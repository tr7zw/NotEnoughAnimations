package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
//? if >= 1.21.11 {

import net.minecraft.client.model.player.*;
//? } else {
/*
import net.minecraft.client.model.*;
*///? }
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;

public class NarutoRunningAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.narutoRunning;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.isSprinting();
    }

    private final BodyPart[] arms = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM };

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return arms;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 500;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        if (part == BodyPart.LEFT_ARM && !AnimationUtil.isSwingingArm(entity, part)) {
            AnimationUtil.applyArmTransforms(model, HumanoidArm.LEFT, 1f, -0.2f, 0.3f);
        }
        if (part == BodyPart.RIGHT_ARM && !AnimationUtil.isSwingingArm(entity, part)) {
            AnimationUtil.applyArmTransforms(model, HumanoidArm.RIGHT, 1f, -0.2f, 0.3f);
        }
    }

}
