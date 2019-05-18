package tcdk.fantasy.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

public class EventListen {
    private Logger logger;
    private Minecraft mc;

    public EventListen(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(this);
        logger=event.getModLog();
        mc=Minecraft.getMinecraft();
    }
}