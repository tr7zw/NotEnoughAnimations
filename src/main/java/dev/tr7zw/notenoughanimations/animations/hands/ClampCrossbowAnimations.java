package dev.tr7zw.notenoughanimations.animations.hands;

import java.util.EnumSet;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import lombok.Getter;
import net.minecraft.client.model.HumanoidModel.ArmPose;
//? if >= 1.21.11 {

import net.minecraft.client.model.player.*;
//? } else {
/*
import net.minecraft.client.model.*;
*///? }
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class ClampCrossbowAnimations extends VanillaProjectileWeaponAnimation {

    @Getter
    private final EnumSet<ArmPose> twoHandedAnimations = EnumSet.of(ArmPose.CROSSBOW_HOLD, ArmPose.CROSSBOW_CHARGE);

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.clampCrossbowAnimations;
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
                || (entity.getMainArm() == HumanoidArm.RIGHT
                        && AnimationUtil.isChargedCrossbow(entity.getOffhandItem()))
                || (entity.getMainArm() == HumanoidArm.LEFT
                        && AnimationUtil.isChargedCrossbow(entity.getMainHandItem()));
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
