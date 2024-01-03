package dev.tr7zw.notenoughanimations;

//spotless:off
//#if FABRIC
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NEAnimationsMod extends NEAnimationsLoader implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        onEnable();
        ClientTickEvents.START_WORLD_TICK.register(event -> this.clientTick());
    }

}
//#elseif FORGE
//$$import dev.tr7zw.notenoughanimations.config.ConfigScreenProvider;
//$$import net.minecraftforge.common.MinecraftForge;
//$$import net.minecraftforge.event.TickEvent;
//$$import net.minecraftforge.event.TickEvent.Phase;
//$$import net.minecraftforge.event.TickEvent.Type;
//$$	
//$$ import net.minecraftforge.fml.ModLoadingContext;
//$$ import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
//$$ import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//$$ import dev.tr7zw.config.CustomConfigScreen;
//$$ import net.minecraftforge.fml.LogicalSide;
//$$
//#if MC <= 11605
//$$ import net.minecraftforge.fml.ExtensionPoint;
//$$ import net.minecraftforge.fml.network.FMLNetworkConstants;
//$$ import org.apache.commons.lang3.tuple.Pair;
//#elseif MC <= 11701
//$$ import net.minecraftforge.fml.IExtensionPoint;
//$$ import net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory;
//#elseif MC <= 11802
//$$ import net.minecraftforge.fml.IExtensionPoint;
//$$ import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
//#else
//$$ import net.minecraftforge.fml.IExtensionPoint;
//$$ import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
//#endif
//$$
//$$public class NEAnimationsMod extends NEAnimationsLoader {
//$$
//$$ public NEAnimationsMod() {
//$$ onEnable();
//#if MC <= 11605
//$$         ModLoadingContext.get().registerExtensionPoint(
//$$ ExtensionPoint.CONFIGGUIFACTORY,
//$$ () -> (mc, screen) -> ConfigScreenProvider.createConfigScreen(screen));
//#elseif MC <= 11802
//$$ ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class, ()
//$$ -> new ConfigGuiFactory((mc, screen) -> {
//$$            return ConfigScreenProvider.createConfigScreen(screen);
//$$        }));
//#else
//$$ ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class, () -> new ConfigScreenFactory((mc, screen) -> {
//$$            return ConfigScreenProvider.createConfigScreen(screen);
//$$        }));
//#endif 
//#if MC <= 11605
//$$ ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
//$$ () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));
//#else
//$$        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
//$$                () -> new IExtensionPoint.DisplayTest(
//$$                       () -> ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString(),
//$$                        (remote, isServer) -> true));
//#endif
//$$ MinecraftForge.EVENT_BUS.addListener(this::doClientTick);
//$$ }
//$$
//$$ private void doClientTick(TickEvent event) {
//$$ if(event.type == Type.PLAYER && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
//$$ this.clientTick();
//$$ }
//$$
//$$}
//$$
//#elseif NEOFORGE
//$$ import dev.tr7zw.notenoughanimations.config.ConfigScreenProvider;
//$$ import net.neoforged.fml.IExtensionPoint;
//$$ import net.neoforged.fml.ModLoadingContext;
//$$ import net.neoforged.neoforge.client.ConfigScreenHandler.ConfigScreenFactory;
//$$ import net.neoforged.neoforge.common.NeoForge;
//$$ import net.neoforged.fml.LogicalSide;
//$$ import net.neoforged.neoforge.event.TickEvent.PlayerTickEvent;
//$$ import net.neoforged.neoforge.event.TickEvent.Phase;
//$$ import net.neoforged.neoforge.event.TickEvent.Type;
//$$
//$$public class NEAnimationsMod extends NEAnimationsLoader {
//$$
//$$ public NEAnimationsMod() {
//$$ onEnable();
//$$
//$$ ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
//$$ () -> new IExtensionPoint.DisplayTest(
//$$ () ->
//$$ ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString(),
//$$ (remote, isServer) -> true));
//$$ ModLoadingContext.get().registerExtensionPoint(ConfigScreenFactory.class,
//$$ () -> new ConfigScreenFactory((mc, screen) -> {
//$$ return ConfigScreenProvider.createConfigScreen(screen);
//$$ }));
//$$ NeoForge.EVENT_BUS.addListener(this::doClientTick);
//$$ }
//$$
//$$ private void doClientTick(PlayerTickEvent event) {
//$$ if(event.type == Type.PLAYER && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
//$$ this.clientTick();
//$$ }
//$$
//$$}
//$$
//#endif