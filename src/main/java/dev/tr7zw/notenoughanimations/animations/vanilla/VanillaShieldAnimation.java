package dev.tr7zw.notenoughanimations.animations.vanilla;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class VanillaShieldAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return true;
    }

    private ArmPose rightArmPose;
    private ArmPose leftArmPose;
    private final BodyPart[] left = new BodyPart[] { BodyPart.LEFT_ARM };
    private final BodyPart[] right = new BodyPart[] { BodyPart.RIGHT_ARM };
    private final BodyPart[] leftFixed = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.BODY };
    private final BodyPart[] rightFixed = new BodyPart[] { BodyPart.RIGHT_ARM, BodyPart.BODY };

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        rightArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        leftArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        return ArmPose.BLOCK == leftArmPose || ArmPose.BLOCK == rightArmPose;
    }

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        if (ArmPose.BLOCK == leftArmPose) {
            return NEABaseMod.config.enableRotationLocking ? leftFixed : left;
        }
        if (ArmPose.BLOCK == rightArmPose) {
            return NEABaseMod.config.enableRotationLocking ? rightFixed : right;
        }
        // ???
        return new BodyPart[0];
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 3100;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        if (part == BodyPart.BODY) {
            data.disableBodyRotation(true);
            entity.setYBodyRot(entity.getYHeadRot());
            entity.yBodyRotO = entity.yHeadRotO;
        }
    }

}
