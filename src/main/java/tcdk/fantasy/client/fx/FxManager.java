package tcdk.fantasy.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FxManager {
    private static ConcurrentLinkedQueue<MyFx> fxqueue=new ConcurrentLinkedQueue<>();

    public static void addFx(MyFx fx){
        fxqueue.offer(fx);
    }

    @SideOnly(Side.CLIENT)
    public static void renderAll(float partialTicks, TextureManager textureManager){
        Minecraft mc=Minecraft.getMinecraft();

        for(MyFx fx:fxqueue){
            if(!fx.isDead()){
                fx.Render(partialTicks,textureManager,
                        fx.posX-(float)mc.player.prevPosX,
                        fx.posY-(float)mc.player.prevPosY,
                        fx.posZ-(float)mc.player.prevPosZ);
            }
        }
    }
    public static void UpdateAll(){
        for(Iterator<MyFx> it=fxqueue.iterator();it.hasNext();){
            MyFx fx=it.next();
            fx.Update();
            if(fx.isDead()){
                it.remove();
            }
        }
    }
}
