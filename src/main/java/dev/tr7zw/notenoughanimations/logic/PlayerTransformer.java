package dev.tr7zw.notenoughanimations.logic;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.RotationLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;

public class PlayerTransformer {

    public static final int ENTRY_SIZE = 9;
    public static final int ENTRY_AMOUNT = 5;

    private boolean doneLatebind = false;
    private final Minecraft mc = Minecraft.getInstance();
    private int tickId = 0; // ok to overflow, just used to keep track of what has been updated this tick
    private boolean renderingFirstPersonArm = false;
    private float deltaTick = 0;

    public void updateModel(AbstractClientPlayer entity, PlayerModel model, float swing, CallbackInfo info) {
        if (!doneLatebind)
            lateBind();
        if (mc.level == null || renderingFirstPersonArm) { // We are in a main menu or something || don't touch the
                                                           // first person model hand
            return;
        }

        NEAnimationsLoader.INSTANCE.animationProvider.applyAnimations(entity, model, deltaTick, swing);

        if (entity instanceof PlayerData data) {
            float[] last = data.getLastRotations();
            int passedTicks = data.isUpdated(tickId);
            boolean differentFrame = passedTicks != 0;
            float timePassed = passedTicks * 50f;
            if (NEABaseMod.config.enableAnimationSmoothing) {
                float speed = NEABaseMod.config.animationSmoothingSpeed;
                interpolate(model.leftArm, last, 0, timePassed, differentFrame, speed, deltaTick);
                interpolate(model.rightArm, last, ENTRY_SIZE, timePassed, differentFrame, speed, deltaTick);
                if (!NEABaseMod.config.disableLegSmoothing) {
                    interpolate(model.leftLeg, last, ENTRY_SIZE * 2, timePassed, differentFrame, speed, deltaTick);
                    interpolate(model.rightLeg, last, ENTRY_SIZE * 3, timePassed, differentFrame, speed, deltaTick);
                }
            }
            if (entity == mc.cameraEntity && !data.isBodyRotationDisabled()) {
                // For now located here due to smoothing logic being here.
                if (NEABaseMod.config.rotationLock == RotationLock.SMOOTH && entity.getVehicle() == null) {
                    interpolateYawBodyHead(entity, last, ENTRY_SIZE * 4, timePassed, differentFrame, 0.5f);
                }
                if (NEABaseMod.config.rotationLock == RotationLock.FIXED && entity.getVehicle() == null
                        && differentFrame) {
                    entity.yBodyRot = entity.yHeadRot;
                    entity.yBodyRotO = entity.yHeadRotO;
                }
            }
            data.setUpdated(tickId);
        }
    }

    public void preUpdate(AbstractClientPlayer livingEntity, PlayerModel playerModel, float swing, CallbackInfo info) {
        if (mc.level == null || renderingFirstPersonArm) { // We are in a main menu or something || don't touch the
                                                           // first person model hand
            return;
        }
        NEAnimationsLoader.INSTANCE.animationProvider.preUpdate(livingEntity, playerModel);
    }

    private void lateBind() {
        NEAnimationsLoader.INSTANCE.animationProvider.refreshEnabledAnimations();
        doneLatebind = true;
    }

    public void nextTick() {
        tickId++;
    }

    public void setDeltaTick(float delta) {
        this.deltaTick = delta;
    }

    public void renderingFirstPersonArm(boolean flag) {
        this.renderingFirstPersonArm = flag;
    }

    private void interpolate(ModelPart model, float[] last, int offset, float timePassed, boolean differentFrame,
            float speed, float delta) {
        // 0, 1, 2 = cur Target. 3,4,5 = last ticks target. 6,7,8 the last rendered
        // target
        if (timePassed > 50) { // Don't try to interpolate states older than 1 tick
            last[offset] = last[offset + 3] = last[offset + 6] = model.xRot;
            last[offset + 1] = last[offset + 4] = last[offset + 7] = model.yRot;
            last[offset + 2] = last[offset + 5] = last[offset + 8] = model.zRot;
            cleanInvalidData(last, offset);
            return;
        }
        if (!differentFrame) { // Rerendering the place in the same frame
            last[offset + 6] = AnimationUtil.lerpAngle(delta, last[offset + 3], last[offset]);
            last[offset + 7] = AnimationUtil.lerpAngle(delta, last[offset + 4], last[offset + 1]);
            last[offset + 8] = AnimationUtil.lerpAngle(delta, last[offset + 5], last[offset + 2]);
            model.xRot = last[offset + 6];
            model.yRot = last[offset + 7];
            model.zRot = last[offset + 8];
            return;
        }
        last[offset + 3] = last[offset] = last[offset + 6];
        last[offset + 4] = last[offset + 1] = last[offset + 7];
        last[offset + 5] = last[offset + 2] = last[offset + 8];

        float amount = speed;
        amount = Math.min(amount, 1);
        amount = Math.max(amount, 0); // "Should" be impossible, but just to be sure
        last[offset] = AnimationUtil.interpolateRotation(model.xRot, last[offset], amount);
        last[offset + 1] = AnimationUtil.interpolateRotation(model.yRot, last[offset + 1], amount);
        last[offset + 2] = AnimationUtil.interpolateRotation(model.zRot, last[offset + 2], amount);
        cleanInvalidData(last, offset);
        last[offset + 6] = AnimationUtil.lerpAngle(delta, last[offset + 3], last[offset]);
        last[offset + 7] = AnimationUtil.lerpAngle(delta, last[offset + 4], last[offset + 1]);
        last[offset + 8] = AnimationUtil.lerpAngle(delta, last[offset + 5], last[offset + 2]);
        model.xRot = last[offset + 6];
        model.yRot = last[offset + 7];
        model.zRot = last[offset + 8];
    }

    private void interpolateYawBodyHead(AbstractClientPlayer entity, float[] last, int offset, float timePassed,
            boolean differentFrame, float speed) {
        if (!differentFrame) { // Rerendering the place in the same frame
            entity.yBodyRot = (last[offset]);
            entity.yBodyRotO = last[offset + 1];
            return;
        }
        if (timePassed > 50) { // Don't try to interpolate states older than 100ms
            last[offset] = entity.yHeadRot;
            return;
        }
        if (entity.yHeadRot - last[offset] > 90) {
            speed *= 0.9f;
        }
        if (entity.yHeadRot - last[offset] < -90) {
            speed *= 0.9f;
        }
        last[offset + 1] = last[offset];
        float amount = speed;
        amount = Math.min(amount, 1);
        entity.yBodyRotO = last[offset];
        last[offset] += (entity.yHeadRot - last[offset]) * amount;
        entity.yBodyRot = (last[offset]);
        // entity.yBodyRotO = entity.yBodyRot;
    }

    /**
     * When using a quickcharge 5 crossbow it is able to cause NaN values to show up
     * because of how broken it is.
     * 
     * @param data
     * @param offset
     */
    private void cleanInvalidData(float[] data, int offset) {
        if (Float.isNaN(data[offset]))
            data[offset] = 0;
        if (Float.isNaN(data[offset + 1]))
            data[offset + 1] = 0;
        if (Float.isNaN(data[offset + 2]))
            data[offset + 2] = 0;
    }

}
