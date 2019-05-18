package tcdk.fantasy.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyLoader {
    public static KeyBinding lock;

    public KeyLoader(FMLPreInitializationEvent event)
    {
        KeyLoader.lock = new KeyBinding("tcdk.key.lock", Keyboard.KEY_Z, "tcdk.key.lock");

        ClientRegistry.registerKeyBinding(KeyLoader.lock);
    }
}
