package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
//? if >= 1.21.11 {

import net.minecraft.world.entity.vehicle.boat.*;
import net.minecraft.client.model.player.*;
//? } else {
/*
import net.minecraft.client.model.*;
import net.minecraft.world.entity.vehicle.*;
*///? }
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class BoatAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.enableRowBoatAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        //? if > 1.21.2 {

        return entity.isPassenger() && entity.getVehicle() instanceof AbstractBoat;
        //? } else {
        /*
         return entity.isPassenger() && entity.getVehicle() instanceof net.minecraft.world.entity.vehicle.Boat;
        *///? }
    }

    private final BodyPart[] bothHands = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM };

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return bothHands;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 1500;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        if (part == BodyPart.BODY) {
            return;
        }
        if (part == BodyPart.LEFT_ARM && AnimationUtil.isSwingingArm(entity, part)) {
            return;
        }
        if (part == BodyPart.RIGHT_ARM && AnimationUtil.isSwingingArm(entity, part)) {
            return;
        }
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        //? if > 1.21.2 {

        AbstractBoat boat = (AbstractBoat) entity.getVehicle();
        //? } else {
        /*
         net.minecraft.world.entity.vehicle.Boat boat = (net.minecraft.world.entity.vehicle.Boat) entity.getVehicle();
        *///? }
        int id = boat.getPassengers().indexOf(entity);
        if (id == 0) {
            float paddle = boat.getRowingTime(arm == HumanoidArm.LEFT ? 0 : 1, delta);
            AnimationUtil.applyArmTransforms(model, arm, -1.1f - Mth.sin(paddle) * 0.3f, 0.2f, 0.3f);
        }
    }

}
