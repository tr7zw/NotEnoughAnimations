package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

import java.util.EnumSet;

public class ClampCrossbowAnimations extends BasicAnimation {

     @Override
     public boolean isEnabled() { return NEABaseMod.config.clampCrossbowAnimations; }

    private ArmPose rightArmPose;
    private ArmPose leftArmPose;
    private final EnumSet<ArmPose> twoHandedAnimations = EnumSet.of(ArmPose.CROSSBOW_HOLD);
    private final BodyPart[] parts = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM, BodyPart.BODY };

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        rightArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        leftArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        return twoHandedAnimations.contains(leftArmPose) || twoHandedAnimations.contains(rightArmPose);
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
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        ModelPart mainArm = model.rightArm;
        ModelPart offArm = model.leftArm;
        BodyPart mainPart = BodyPart.RIGHT_ARM;
        BodyPart offPart = BodyPart.LEFT_ARM;
        boolean bowInLeftHand = (entity.getMainArm() == HumanoidArm.RIGHT
                && entity.getUsedItemHand() == InteractionHand.OFF_HAND)
                || (entity.getMainArm() == HumanoidArm.LEFT && entity.getUsedItemHand() == InteractionHand.MAIN_HAND)
                || (entity.getMainArm() == HumanoidArm.RIGHT && AnimationUtil.isChargedCrossbow(entity.getOffhandItem()))
                || (entity.getMainArm() == HumanoidArm.LEFT && AnimationUtil.isChargedCrossbow(entity.getMainHandItem()));
        if (bowInLeftHand) {
            mainArm = model.leftArm;
            offArm = model.rightArm;
            mainPart = BodyPart.LEFT_ARM;
            offPart = BodyPart.RIGHT_ARM;
        }

        if (part == mainPart) {
            mainArm.xRot = Mth.clamp(AnimationUtil.wrapDegrees(mainArm.xRot), -1.75f, 0f);
        }

        if (part == offPart) {
            offArm.xRot = Mth.clamp(AnimationUtil.wrapDegrees(offArm.xRot), -1.75f, 0f);
        }
    }

}
