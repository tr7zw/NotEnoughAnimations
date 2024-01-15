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
//#else
//#if FORGE
//$$ import net.minecraftforge.fml.LogicalSide;
//$$ import net.minecraftforge.event.TickEvent.PlayerTickEvent;
//$$ import net.minecraftforge.event.TickEvent.Phase;
//$$ import net.minecraftforge.event.TickEvent.Type;
//#elseif NEOFORGE
//$$ import net.neoforged.fml.LogicalSide;
//$$ import net.neoforged.neoforge.event.TickEvent.PlayerTickEvent;
//$$ import net.neoforged.neoforge.event.TickEvent.Phase;
//$$ import net.neoforged.neoforge.event.TickEvent.Type;
//#endif
//$$ import net.minecraft.client.Minecraft;
//$$ import dev.tr7zw.util.ModLoaderUtil;
//$$
//$$
//$$public class NEAnimationsMod extends NEAnimationsLoader {
//$$
//$$ public NEAnimationsMod() {
//$$ ModLoaderUtil.registerForgeEvent(this::doClientTick);
//$$ }
//$$
//$$ private void doClientTick(PlayerTickEvent event) {
//$$ if(event.type == Type.PLAYER && event.player == Minecraft.getInstance().player && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
//$$ this.clientTick();
//$$ }
//$$
//$$}
//$$
//#endif