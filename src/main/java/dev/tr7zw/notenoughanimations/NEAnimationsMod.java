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
//#if MC < 12005
//$$ import net.neoforged.neoforge.event.TickEvent.PlayerTickEvent;
//$$ import net.neoforged.neoforge.event.TickEvent.Phase;
//$$ import net.neoforged.neoforge.event.TickEvent.Type;
//#else
//$$ import net.neoforged.neoforge.event.tick.PlayerTickEvent;
//#endif
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
//#if MC >= 12005 && NEOFORGE
//$$ private void doClientTick(PlayerTickEvent.Pre event) {
//$$ if(event.getEntity() == Minecraft.getInstance().player && event.getEntity().level().isClientSide())
//#else
//$$ private void doClientTick(PlayerTickEvent event) {
//$$ if(event.type == Type.PLAYER && event.player == Minecraft.getInstance().player && event.side == LogicalSide.CLIENT && event.phase == Phase.START)
//#endif
//$$ this.clientTick();
//$$ }
//$$
//$$}
//$$
//#endif