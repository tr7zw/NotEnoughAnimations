package dev.tr7zw.notenoughanimations.animations.hands;

import java.util.EnumSet;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import dev.tr7zw.notenoughanimations.versionless.animations.BowAnimation;
import dev.tr7zw.transition.mc.EntityUtil;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.geom.ModelPart;
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
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        ModelPart mainArm = model.rightArm;
        ModelPart offArm = model.leftArm;
        BodyPart mainPart = BodyPart.RIGHT_ARM;
        BodyPart offPart = BodyPart.LEFT_ARM;
        boolean bowInLeftHand = (entity.getMainArm() == HumanoidArm.RIGHT && entity.getUsedItemHand() == InteractionHand.OFF_HAND) ||
                (entity.getMainArm() == HumanoidArm.LEFT && entity.getUsedItemHand() == InteractionHand.MAIN_HAND); 
        if (bowInLeftHand) {
            mainArm = model.leftArm;
            offArm = model.rightArm;
            mainPart = BodyPart.LEFT_ARM;
            offPart = BodyPart.RIGHT_ARM;
        }
        int invert = bowInLeftHand ? -1 : 1;

        if (part == mainPart) {
            mainArm.yRot = invert * Mth.clamp(-0.1F + AnimationUtil.wrapDegrees(-model.head.xRot), -1.25f, 0.5f);
            mainArm.xRot = Mth.clamp(-1.5707964F + (invert * AnimationUtil.wrapDegrees(model.head.yRot)), -2f, 0);
            mainArm.zRot += invert * 1.5F;
        }

        if (part == offPart) {
            offArm.yRot = invert * Mth.clamp(0.1F + AnimationUtil.wrapDegrees(-model.head.xRot), -1.05f, 0.7f);
            offArm.xRot = Mth.clamp(-1.5707964F + (invert * AnimationUtil.wrapDegrees(model.head.yRot)) + 0.8f, -1.05f, -0.65f);
            offArm.zRot += invert * 1.5F;
        }
        
        if (part == BodyPart.BODY && NEABaseMod.config.customBowRotationLock) {
            if(bowInLeftHand) {
                entity.yBodyRot = EntityUtil.getYRot(entity) + 40;
                entity.yBodyRotO = entity.yRotO + 40;
            } else {
                entity.yBodyRot = EntityUtil.getYRot(entity) - 40;
                entity.yBodyRotO = entity.yRotO - 40;
            }
        }
    }

}
