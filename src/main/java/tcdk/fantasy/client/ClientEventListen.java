package tcdk.fantasy.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import tcdk.fantasy.Fantasy;
import tcdk.fantasy.Util.MathHelp;
import tcdk.fantasy.client.fx.FxManager;

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
        EntityPlayer player= Minecraft.getMinecraft().player;
        if(event.getEntity()!=player){
            float rtick=event.getPartialRenderTick();
            Vec3d start=player.getPositionEyes(rtick);
            Vec3d end=player.getLook(rtick).scale(6).add(start);
            boolean res= MathHelp.isEntityOnPath(event.getEntity(),start,end);
            if (res){
                randerTag(event);
            }
        }
    }

    @SubscribeEvent
    public void InputEvent (InputEvent.MouseInputEvent event) {
            //NetLoader.instance.sendToServer(new SlashMsg());
    }

    @SubscribeEvent
    public void CameraSetup(EntityViewRenderEvent.CameraSetup event){

    }

    @SubscribeEvent
    public void RenderLast(RenderWorldLastEvent event){

        float partialTicks=event.getPartialTicks();
        TextureManager textureManager=mc.getTextureManager();
        FxManager.renderAll(partialTicks,textureManager);
    }
}