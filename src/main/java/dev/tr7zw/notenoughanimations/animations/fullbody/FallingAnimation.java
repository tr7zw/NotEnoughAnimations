package dev.tr7zw.notenoughanimations.animations.fullbody;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import dev.tr7zw.notenoughanimations.versionless.animations.DataHolder;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;

public class FallingAnimation extends BasicAnimation
        implements DataHolder<dev.tr7zw.notenoughanimations.animations.fullbody.FallingAnimation.FallingData> {

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.fallingAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        //#if MC >= 11700
        if (!entity.isFallFlying() && !entity.onGround() && !entity.onClimbable() && !entity.getAbilities().flying
        //#else
        //$$ if (!entity.isFallFlying() && !entity.isOnGround() && !entity.onClimbable() && !entity.abilities.flying
        //#endif
                && !entity.isSwimming()) {
            FallingData fallData = data.getData(this, () -> new FallingData(entity.getY()));
            if (entity instanceof LocalPlayer) {
                fallData.fallingSpeed = (float) (entity.getDeltaMovement().lengthSqr() / 11);
                return entity.fallDistance > 3;
            }
            if (entity.getY() == fallData.lastY) { // rerender in same tick
                return fallData.fallingSpeed > 0.5f / 3.5f;
            } else {
                fallData.fallingSpeed = (float) (fallData.lastY - entity.getY()) / 3.5f;
                fallData.lastY = entity.getY();
                return fallData.fallingSpeed > 0.5f / 3.5f;
            }
        }
        return false;
    }

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return BodyPart.values();
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 400;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        FallingData fallData = data.getData(this, () -> new FallingData(entity.getY()));
        float moveSqrt = fallData.fallingSpeed;
        float armsMove = Math.min(1, moveSqrt * 2);
        moveSqrt = Math.min(1, moveSqrt);
        float moveOutArms = 1.9F * armsMove;
        float moveOutLegs = 0.6F * moveSqrt;

        float movement = entity.tickCount + delta;
        if (part == BodyPart.LEFT_ARM) {
            model.leftArm.xRot = Mth.cos(movement * 0.6662F) * moveSqrt;
            model.leftArm.zRot = -moveOutArms;
        }
        if (part == BodyPart.RIGHT_ARM) {
            model.rightArm.xRot = Mth.cos(movement * 0.6662F + 3.1415927F) * moveSqrt;
            model.rightArm.zRot = moveOutArms;
        }
        if (part == BodyPart.LEFT_LEG) {
            model.leftLeg.xRot = Mth.cos(movement * 0.6662F + 3.1415927F) * 1.4F * moveSqrt;
            model.leftLeg.zRot = -moveOutLegs;
        }
        if (part == BodyPart.RIGHT_LEG) {
            model.rightLeg.xRot = Mth.cos(movement * 0.6662F) * 1.4F * moveSqrt;
            model.rightLeg.zRot = moveOutLegs;
        }
    }

    public static class FallingData {
        public FallingData(double y) {
            this.lastY = y;
        }

        public double lastY = 0;
        public float fallingSpeed = 0;
    }

}
