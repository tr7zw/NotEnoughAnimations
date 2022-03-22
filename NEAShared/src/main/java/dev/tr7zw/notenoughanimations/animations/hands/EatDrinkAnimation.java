package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.UseAnim;

public class EatDrinkAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.enableEatDrinkAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        if (entity.getUseItemRemainingTicks() > 0) {
            UseAnim action = entity.getUseItem().getUseAnimation();
            // Eating/Drinking
            if (action == UseAnim.EAT || action == UseAnim.DRINK) {
                if(entity.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                    target = entity.getMainArm() == HumanoidArm.RIGHT ? right : left;
                } else {
                    target = entity.getMainArm() == HumanoidArm.LEFT ? right : left;
                }
                return true;
            }
        }
        return false;
    }

    private final BodyPart[] left = new BodyPart[] {BodyPart.LEFT_ARM};
    private final BodyPart[] right = new BodyPart[] {BodyPart.RIGHT_ARM};
    private BodyPart[] target = right;
    
    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return target;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 2500;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        AnimationUtil.applyArmTransforms(model, arm,
                -(Mth.lerp(-1f * (entity.getXRot() - 90f) / 180f, 1f, 2f)) + Mth.sin((System.currentTimeMillis() % 20000) / 30f) * 0.1f,
                -0.3f, 0.3f);
    }

}
