package tcdk.fantasy.client.fx;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MyFx {
    public float posX,posY,posZ;
    public Vec3d speedVec;
    private boolean dead;
    private int life;

    public MyFx(float posX,float posY,float posZ,Vec3d speedVec,int life){
        this.posX=posX;
        this.posY=posY;
        this.posZ=posZ;
        this.speedVec=speedVec;
        this.dead=false;
        this.life=life;
    }

    public void Update(){
        posX+=speedVec.x;
        posY+=speedVec.y;
        posZ+=speedVec.z;
        life--;
        if(life<=0){
            dead=true;
        }
    }

    @SideOnly(Side.CLIENT)
    public abstract void Render(float partialTicks, TextureManager textureManager,
                                float posX,float posY,float posZ);

    public boolean isDead(){return dead;}
    public void Dead(){dead=true;}
}
