package dev.tr7zw.notenoughanimations;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NEAnimationsMod extends NEAnimationsLoader implements ModInitializer {
    @Override
    public void onInitialize() {
        onEnable();
        ClientTickEvents.START_CLIENT_TICK.register(event -> this.clientTick());
    }

}
