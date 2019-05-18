package tcdk.fantasy.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SlashMsgHandler implements IMessageHandler<SlashMsg, IMessage> {

    @Override
    public IMessage onMessage(SlashMsg message, MessageContext ctx) {
        EntityPlayerMP player=ctx.getServerHandler().player;
        double dx = (double)(-MathHelper.sin(player.rotationYaw * (float)(Math.PI/180.0f)));
        double dz = (double)MathHelper.cos(player.rotationYaw * (float)(Math.PI/180.0f));
        ((WorldServer)player.world).spawnParticle(
                EnumParticleTypes.SWEEP_ATTACK,
                player.posX + dx,
                player.posY + player.height * 0.5D,
                player.posZ + dz,
                0, dx, 0, dz, 0.0D);
        return null;
    }
}
