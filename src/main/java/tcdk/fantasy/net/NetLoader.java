package tcdk.fantasy.net;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetLoader {

    private static int nextID = 0;

    public static SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("fantasy_ex");

    public NetLoader(FMLPreInitializationEvent event)
    {
        instance.registerMessage(SlashMsgHandler.class, SlashMsg.class, nextID++, Side.SERVER);
    }
}
