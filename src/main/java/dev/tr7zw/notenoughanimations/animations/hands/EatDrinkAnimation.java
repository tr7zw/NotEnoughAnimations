package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import dev.tr7zw.util.NMSHelper;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemUseAnimation;

public class EatDrinkAnimation extends BasicAnimation {

    private BodyPart[] target;
    private final BodyPart[] left = new BodyPart[] { BodyPart.LEFT_ARM };
    private final BodyPart[] right = new BodyPart[] { BodyPart.RIGHT_ARM };

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.enableEatDrinkAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        if (entity.getUseItemRemainingTicks() > 0) {
            ItemUseAnimation action = entity.getUseItem().getUseAnimation();
            // Eating/Drinking
            if (action == ItemUseAnimation.EAT || action == ItemUseAnimation.DRINK) {
                if (entity.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                    if (entity.getMainArm() == HumanoidArm.RIGHT) {
                        target = right;
                    } else {
                        target = left;
                    }
                } else {
                    if (entity.getMainArm() == HumanoidArm.RIGHT) {
                        target = left;
                    } else {
                        target = right;
                    }
                }
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
        return 2500;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        float g = entity.getUseItemRemainingTicks() - delta + 1.0F;
        //        float h = g / entity.getUseItem().getUseDuration();
        AnimationUtil.applyArmTransforms(model, arm, -(Mth.lerp(-1f * (NMSHelper.getXRot(entity) - 90f) / 180f, 1f, 2f))
                + Mth.abs(Mth.cos(g / 4.0F * 3.1415927F) * 0.2F), -0.3f, 0.3f);
    }

}
