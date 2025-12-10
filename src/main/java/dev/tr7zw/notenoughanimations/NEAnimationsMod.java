package dev.tr7zw.notenoughanimations;

public class NEAnimationsMod extends NEAnimationsLoader
        //? if fabric {

        implements net.fabricmc.api.ClientModInitializer
//? }
{
    //? if fabric {

    @Override
    //? }
    public void onInitializeClient() {
        onEnable();
    }

}
