package dev.tr7zw.notenoughanimations.animations.fullbody;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;

public class FallingAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.fallingAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.fallDistance > 3 && !entity.isFallFlying() && !entity.isOnGround() && !entity.onClimbable() && !entity.getAbilities().flying && !entity.isSwimming();
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
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        float moveSqrt = (float) entity.getDeltaMovement().lengthSqr();
        moveSqrt /= 11;
        float armsMove = Math.min(1, moveSqrt*2);
        moveSqrt = Math.min(1, moveSqrt);
        float moveOutArms = 1.9F * armsMove;
        float moveOutLegs = 0.6F * moveSqrt;
        
        float movement = entity.tickCount + delta;
        if(part == BodyPart.LEFT_ARM) {
            model.leftArm.xRot = Mth.cos(movement * 0.6662F) * moveSqrt;
            model.leftArm.zRot = -moveOutArms;
        }
        if(part == BodyPart.RIGHT_ARM) {
            model.rightArm.xRot = Mth.cos(movement * 0.6662F + 3.1415927F) * moveSqrt;
            model.rightArm.zRot = moveOutArms;
        }
        if(part == BodyPart.LEFT_LEG) {
            model.leftLeg.xRot = Mth.cos(movement * 0.6662F + 3.1415927F) * 1.4F * moveSqrt;
            model.leftLeg.zRot = -moveOutLegs;
        }
        if(part == BodyPart.RIGHT_LEG) {
            model.rightLeg.xRot = Mth.cos(movement * 0.6662F) * 1.4F * moveSqrt;
            model.rightLeg.zRot = moveOutLegs;
        }
    }

}
