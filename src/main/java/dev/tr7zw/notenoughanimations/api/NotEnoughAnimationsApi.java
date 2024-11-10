package dev.tr7zw.notenoughanimations.api;

import dev.tr7zw.notenoughanimations.NEAnimationsMod;

public class NotEnoughAnimationsApi {

    /**
     * Register a new {@link BasicAnimation} to the mod. BasicAnimations can
     * implement {@link PoseOverwrite} to access these features.
     * 
     * @param animation
     */
    public static void registerAnimation(BasicAnimation animation) {
        NEAnimationsMod.INSTANCE.animationProvider.addAnimation(animation);
    }

    /**
     * Refreshes which animations are enabled right now. Should be called after
     * {@link BasicAnimation#isEnabled()} might have changed.
     */
    public static void refreshEnabledAnimations() {
        NEAnimationsMod.INSTANCE.animationProvider.refreshEnabledAnimations();
    }

}
