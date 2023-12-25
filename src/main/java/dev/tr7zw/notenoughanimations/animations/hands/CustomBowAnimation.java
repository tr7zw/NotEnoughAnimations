package dev.tr7zw.notenoughanimations.animations.hands;

import java.util.EnumSet;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import dev.tr7zw.notenoughanimations.versionless.animations.BowAnimation;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class CustomBowAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.bowAnimation == BowAnimation.CUSTOM_V1;
    }

    private ArmPose rightArmPose;
    private ArmPose leftArmPose;
    private final EnumSet<ArmPose> twoHandedAnimatios = EnumSet.of(ArmPose.BOW_AND_ARROW);
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
        return 3200;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        model.rightArm.yRot = Mth.clamp(-0.1F + -model.head.xRot, -1.25f, 0.5f);
        model.leftArm.yRot = Mth.clamp(0.1F + -model.head.xRot, -1.05f, 0.7f);
        model.rightArm.xRot = Mth.clamp(-1.5707964F + model.head.yRot, -2f, 0);
        model.leftArm.xRot = Mth.clamp(-1.5707964F + model.head.yRot + 0.8f, -1.05f, -0.65f);
        model.rightArm.zRot += 0.5F;
        model.leftArm.zRot += 0.5F;
    }

}
