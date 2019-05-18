package tcdk.fantasy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Fantasy.MODID, name = Fantasy.NAME, version = Fantasy.VERSION)
public class Fantasy
{
    public static final String MODID = "fantasy";
    public static final String NAME = "Fantasy-Ex";
    public static final String VERSION = "1.0.0.ex";
    public static final long initTime=System.currentTimeMillis();

    @SidedProxy(
            clientSide = "tcdk.fantasy.ClientProxy",
            serverSide = "tcdk.fantasy.CommonProxy")
    private static CommonProxy proxy=new CommonProxy();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}