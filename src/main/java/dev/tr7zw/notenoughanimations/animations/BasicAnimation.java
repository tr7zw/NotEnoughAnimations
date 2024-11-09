package dev.tr7zw.notenoughanimations.animations;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public abstract class BasicAnimation {

    private boolean isPrepared = false;

    public abstract boolean isEnabled();

    public abstract boolean isValid(AbstractClientPlayer entity, PlayerData data);

    /**
     * Gets the body parts this animation should be applied to, expects that isValid
     * is called before.
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

    protected void precalculate(AbstractClientPlayer entity, PlayerData data, PlayerModel model, float delta,
            float tickCounter) {
        // not used by default
    }

    public abstract void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part,
            float delta, float tickCounter);

}
