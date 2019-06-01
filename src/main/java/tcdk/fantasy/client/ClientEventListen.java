package tcdk.fantasy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import tcdk.fantasy.Fantasy;
import tcdk.fantasy.Util.MathHelp;

//TODO CameraSetup RenderWorldLastEvent
@SideOnly(Side.CLIENT)
public class ClientEventListen {
    private Logger logger;
    private Minecraft mc;

    public ClientEventListen(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
        logger=event.getModLog();
        mc=Minecraft.getMinecraft();
    }

    private static ResourceLocation swordtext
            =new ResourceLocation("textures/items/diamond_sword.png");
    /**
     * 在生物的头上绘制一个图标，并且随时间沿着Y轴旋转。
     * */
    private void randerTag(RenderLivingEvent event){
        double x=event.getX();
        double y=event.getY();
        double z=event.getZ();
        float tick=(System.currentTimeMillis()- Fantasy.initTime)/1000.0F;

        event.getRenderer().bindTexture(swordtext);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y+event.getEntity().height, (float)z);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableCull();

        GlStateManager.rotate((tick%3)*120.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.3D, 0.9D, 0).tex(0.0D,0.0D).endVertex();
        bufferbuilder.pos(-0.3D, 0.9D, 0).tex(1.0, 0.0D).endVertex();
        bufferbuilder.pos(-0.3D, 0.3D, 0).tex(1.0D, 1.0).endVertex();
        bufferbuilder.pos(0.3D, 0.3D, 0).tex(0.0D, 1.0).endVertex();
        tessellator.draw();

        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void renderLivingEvent(RenderLivingEvent.Pre event) {
        EntityPlayerSP player= Minecraft.getMinecraft().player;
        if(event.getEntity()!=player && player.getHeldItemMainhand().getItem() instanceof ItemSword){
            float rtick=event.getPartialRenderTick();
            Vec3d start=player.getPositionEyes(rtick);
            Vec3d end=player.getLook(rtick).scale(5).add(start);
            boolean res= MathHelp.isEntityOnPath(event.getEntity(),start,end);
            if (res){
                randerTag(event);
            }
        }
    }

    //TODO 做一个蓄力的粒子效果。若蓄力完成就会产生剑气。普通的剑攻击变成一个范围型攻击。
    private boolean isPress=false,lock=false,swing=false;
    private Vec3d attackVec=new Vec3d(0,0,0);
    private int strength=0;

    //分级
    private int level(Vec3d attackVec,Vec3d dv){
        double sv=dv.dotProduct(attackVec);
        double abssv=sv<0?-sv:sv;
        abssv=Math.max(abssv,1);
        if(sv<0){
            return -1-(int)(Math.log10(abssv)/0.3);
        }else {
            return 1+(int)(Math.log10(abssv)/0.3);
        }
    }

    @SubscribeEvent
    public void InputEvent (MouseEvent event) {
        EntityPlayerSP player= mc.player;
        int button=event.getButton();
        boolean state=event.isButtonstate();
        if(button==0 && player.getHeldItemMainhand().getItem() instanceof ItemSword){
            event.setCanceled(true);
            if(player.getCooledAttackStrength(0)==1.0 && state) {
                //event.isButtonstate可以判断是按下还是放开
                //按下，进入锁定视角状态，初始化偏移值和方向。开始记录鼠标偏移
                isPress = true;
                lock=true;
                swing=true;
            } else if(isPress && !state){
                isPress=false;
                //放开，开始记录鼠标记录反向鼠标偏移。
            }
        }else if(button==-1 && player.getHeldItemMainhand().getItem() instanceof ItemSword && swing){
            int dx=event.getDx();
            int dy=event.getDy();
            Vec3d dv=new Vec3d(dx,dy,0);
            logger.info("["+dx+":"+dy+":"+event.getX()+":"+event.getY()+"]");

            if(isPress){
                if(strength<=0){
                    //初始化方向
                    attackVec=dv.normalize();
                    strength=1;

                    logger.info("init attackVec:"+attackVec);
                }else{
                    strength=Math.max(level(attackVec,dv),strength);
                    logger.info("inc strength:"+strength);
                }
            }else{
                double decStrength=Math.min(level(attackVec,dv),0);
                logger.info("dec strength:"+decStrength);
                if(strength+decStrength<=0){
                    //已经将之前蓄力释放完成，发出攻击动作
                    player.resetCooldown();
                    strength=0;
                    swing=false;
                    //TODO 攻击
                    logger.info("attack");
                }
            }
        }
    }

    @SubscribeEvent
    public void RenderStart(TickEvent.RenderTickEvent event){
        if(event.phase== TickEvent.Phase.START && lock){
            //在Mouse中，XY的值被取过之后会被重置为0
            // 利用这一点，在渲染一开始取一次，使得后面的逻辑认为鼠标没有移动。
            //以此达到锁定视角的作用
            mc.mouseHelper.mouseXYChange();
        }
    }

    //在镜头移动的时候触发。可以利用来实现锁定视角一直直视某一个怪物
    @SubscribeEvent
    public void CameraSetup(EntityViewRenderEvent.CameraSetup event){
    }

    @SubscribeEvent
    public void RenderLast(RenderWorldLastEvent event){
        EntityPlayerSP player=mc.player;
        if(player.getHeldItemMainhand().getItem() instanceof ItemSword
                && lock && !swing
                && player.getCooledAttackStrength(0)==1.0){
            lock=false;
        }
    }
}
