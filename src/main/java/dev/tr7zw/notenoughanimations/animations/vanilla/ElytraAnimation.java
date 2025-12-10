package dev.tr7zw.notenoughanimations.animations.vanilla;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.api.PoseOverwrite;
import dev.tr7zw.notenoughanimations.util.RenderStateHolder;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;

public class ElytraAnimation extends BasicAnimation implements PoseOverwrite {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.getPose() == Pose.FALL_FLYING;
    }

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return BodyPart.values();
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 3600;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        if (!NEABaseMod.config.tweakElytraAnimation) {
            // Do nothing
            return;
        }
        // move arms and legs a bit to the side
        float k = (float) entity.getDeltaMovement().lengthSqr();
        k /= 0.2F;
        k *= k * k;
        if (k < 1.0F)
            k = 1.0F;
        float moveOut = 0.1507964F / k;
        moveOut = Math.min(moveOut, 0.25f);
        moveOut = Math.max(moveOut, 0.1F);
        if (part == BodyPart.LEFT_ARM) {
            model.leftArm.xRot = Mth.cos(tickCounter * 0.6662F) * 0.5F / k;
            model.leftArm.zRot = -moveOut;
        }
        if (part == BodyPart.RIGHT_ARM) {
            model.rightArm.xRot = Mth.cos(tickCounter * 0.6662F + 3.1415927F) * 0.5F / k;
            model.rightArm.zRot = moveOut;
        }
        if (part == BodyPart.LEFT_LEG) {
            model.leftLeg.xRot = Mth.cos(tickCounter * 0.6662F + 3.1415927F) * 0.7F / k;
            model.leftLeg.zRot = -moveOut;
        }
        if (part == BodyPart.RIGHT_LEG) {
            model.rightLeg.xRot = Mth.cos(tickCounter * 0.6662F) * 0.7F / k;
            model.rightLeg.zRot = moveOut;
        }
    }

    @Override
    public void updateState(AbstractClientPlayer entity, PlayerData data, PlayerModel playerModel) {
        if (isValid(entity, data)) {
            //? if >= 1.21.2 {

            RenderStateHolder.RenderStateData stateData = data.getData(RenderStateHolder.INSTANCE,
                    RenderStateHolder.RenderStateData::new);
            stateData.renderState.isCrouching = false;
            //? } else {
            /*
             playerModel.crouching = false;
            *///? }
        }
    }

}
