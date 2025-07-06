package dev.tr7zw.notenoughanimations;

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
    }

}