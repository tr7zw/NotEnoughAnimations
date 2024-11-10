package dev.tr7zw.notenoughanimations.animations.vanilla;

import java.util.EnumSet;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class VanillaTwoHandedAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return true;
    }

    private ArmPose rightArmPose;
    private ArmPose leftArmPose;
    private final EnumSet<ArmPose> twoHandedAnimatios = EnumSet.of(ArmPose.CROSSBOW_CHARGE, ArmPose.BOW_AND_ARROW,
            ArmPose.CROSSBOW_HOLD);
    private final BodyPart[] parts = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM, BodyPart.BODY };

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        rightArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        leftArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        return twoHandedAnimatios.contains(leftArmPose) || twoHandedAnimatios.contains(rightArmPose);
    }

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return parts;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 3100;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        // Vanilla, do nothing
    }

}
