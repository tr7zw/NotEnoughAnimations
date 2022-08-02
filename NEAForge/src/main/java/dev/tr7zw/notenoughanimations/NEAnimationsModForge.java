package dev.tr7zw.notenoughanimations;

import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod("notenoughanimations")
public class NEAnimationsModForge extends NEAnimationsLoader {

    public NEAnimationsModForge() {
        try {
            Class clientClass = net.minecraft.client.Minecraft.class;
        } catch (Throwable ex) {
            LOGGER.warn("Not Enough Animations Mod installed on a Server. Going to sleep.");
            return;
        }
        onEnable();
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString(),
                        (remote, isServer) -> true));
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((mc, screen) -> {
                    return createConfigScreen(screen);
                }));
    }

}
