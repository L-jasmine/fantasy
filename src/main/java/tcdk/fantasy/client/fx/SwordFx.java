package tcdk.fantasy.client.fx;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SwordFx extends MyFx {
    private static ResourceLocation swordtext
            =new ResourceLocation("textures/items/diamond_sword.png");

    public SwordFx(float posX, float posY, float posZ, Vec3d speedVec,int life) {
        super(posX, posY, posZ, speedVec,life);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void Render(float partialTicks, TextureManager textureManager,
                       float x,float y,float z) {
        textureManager.bindTexture(swordtext);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableCull();
        GlStateManager.translate(x, y, z);

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-2D, -2D, 0).tex(0.0D,0.0D).endVertex();
        bufferbuilder.pos(2D, -2D, 0).tex(1.0, 0.0D).endVertex();
        bufferbuilder.pos(2D, 2D, 0).tex(1.0D, 1.0).endVertex();
        bufferbuilder.pos(-2D, 2D, 0).tex(0.0D, 1.0).endVertex();
        tessellator.draw();

        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
