package dev.tr7zw.notenoughanimations;

import dev.tr7zw.transition.loader.ModLoaderEventUtil;

public class NEAnimationsMod extends NEAnimationsLoader
        //#if FABRIC
        implements net.fabricmc.api.ClientModInitializer
//#endif
{
    //#if FABRIC
    @Override
    //#endif
    public void onInitializeClient() {
        onEnable();
        ModLoaderEventUtil.registerWorldTickStartListener(() -> this.clientTick());
    }

}