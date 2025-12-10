package dev.tr7zw.notenoughanimations.api;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
//? if >= 1.21.11 {

import net.minecraft.client.model.player.*;
//? } else {
/*
import net.minecraft.client.model.*;
*///? }
import net.minecraft.client.player.AbstractClientPlayer;

public abstract class BasicAnimation {

    private boolean isPrepared = false;

    /**
     * Check if the animation is enabled in the settings.
     * 
     * @return true when enabled
     */
    public abstract boolean isEnabled();

    /**
     * Check if this animation wants to apply to the provided player right now. When
     * true, the priorities determine if and which body parts will be assigned to
     * this animation.
     * 
     * @param entity
     * @param data
     * @return
     */
    public abstract boolean isValid(AbstractClientPlayer entity, PlayerData data);

    /**
     * Gets the body parts this animation should be applied to, expects that isValid
     * is called before, so state can be cached from isValid to this method.
     * 
     * @param entity
     * @param data
     * @return
     */
    public abstract BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data);

    /**
     * Higher priority = overwrites other animations. -1 = Don't play 0 = not used,
     * falls back to vanilla 1-999 = low priority passive animations(item holding
     * for example) 1000-1999 = active side animations(boats, ladder) 2000-2999 =
     * active direct animations(eating food) 3000-3999 = Highly important
     * animations(vanilla bow animation for example)
     * 
     * @param entity
     * @param data
     * @return
     */
    public abstract int getPriority(AbstractClientPlayer entity, PlayerData data);

    public void prepare(AbstractClientPlayer entity, PlayerData data, PlayerModel model, float delta, float swing) {
        if (!isPrepared) {
            precalculate(entity, data, model, delta, swing);
            isPrepared = true;
        }
    }

    public void cleanup() {
        isPrepared = false;
    }

    /**
     * This method allows to precalculate data to be then used in the apply method
     * to reduce overhead.
     * 
     * @param entity
     * @param data
     * @param model
     * @param delta
     * @param tickCounter
     */
    protected void precalculate(AbstractClientPlayer entity, PlayerData data, PlayerModel model, float delta,
            float tickCounter) {
        // not used by default
    }

    /**
     * Apply the animation to the provided bodypart.
     * 
     * @param entity
     * @param data
     * @param model
     * @param part
     * @param delta
     * @param tickCounter
     */
    public abstract void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part,
            float delta, float tickCounter);

}
