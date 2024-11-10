package dev.tr7zw.notenoughanimations.animations.fullbody;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.item.ItemUseAnimation;

public class ActionRotationLockAnimation extends BasicAnimation {

    private BodyPart[] target = new BodyPart[] { BodyPart.BODY };

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.enableRotationLocking;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        if (entity.getUseItemRemainingTicks() > 0) {
            ItemUseAnimation action = entity.getUseItem().getUseAnimation();
            // Eating/Drinking
            if (action == ItemUseAnimation.EAT || action == ItemUseAnimation.DRINK || action == ItemUseAnimation.BLOCK) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return target;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        // lower than animations like ladder
        return 1250;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        data.setRotateBodyToHead(true);
    }

}
