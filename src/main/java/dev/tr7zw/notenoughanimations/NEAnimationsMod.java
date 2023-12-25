package dev.tr7zw.notenoughanimations;

//spotless:off
//#if FABRIC
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NEAnimationsMod extends NEAnimationsLoader implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        onEnable();
        ClientTickEvents.START_CLIENT_TICK.register(event -> this.clientTick());
    }

}
//#elseif FORGE
//$$import dev.tr7zw.notenoughanimations.config.ConfigScreenProvider;
//$$import net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory;
//$$import net.minecraftforge.common.MinecraftForge;
//$$import net.minecraftforge.event.TickEvent.ClientTickEvent;
//$$import net.minecraftforge.fml.IExtensionPoint;
//$$import net.minecraftforge.fml.ModLoadingContext;
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
//$$ MinecraftForge.EVENT_BUS.addListener(this::doClientTick);
//$$ }
//$$
//$$ private void doClientTick(ClientTickEvent event) {
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
//$$ import net.neoforged.neoforge.event.TickEvent.ClientTickEvent;
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
//$$    private void doClientTick(ClientTickEvent event) {
//$$ this.clientTick();
//$$ }
//$$
//$$}
//$$
//#endif