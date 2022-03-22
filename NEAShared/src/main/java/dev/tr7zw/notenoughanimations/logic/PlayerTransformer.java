package dev.tr7zw.notenoughanimations.logic;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.RotationLock;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;

public class PlayerTransformer {

    private boolean doneLatebind = false;
    private final Minecraft mc = Minecraft.getInstance();
    private int frameId = 0; // ok to overflow, just used to keep track of what has been updated this frame
    private boolean renderingFirstPersonArm = false;

    public void updateModel(AbstractClientPlayer entity, PlayerModel<AbstractClientPlayer> model, float idk, float swing,
            CallbackInfo info) {
        if (!doneLatebind)
            lateBind();
        if (mc.level == null || renderingFirstPersonArm) { // We are in a main menu or something || don't touch the first person model hand
            return;
        }
        float deltaTick = Minecraft.getInstance().getDeltaFrameTime();
        NEAnimationsLoader.INSTANCE.animationProvider.applyAnimations(entity, model, deltaTick, swing);

        if (NEAnimationsLoader.config.enableAnimationSmoothing && entity instanceof PlayerData) {
            PlayerData data = (PlayerData) entity;
            float[] last = data.getLastRotations();
            boolean differentFrame = !data.isUpdated(frameId);
            long passedNs = System.nanoTime() - data.lastUpdate();
            float timePassed = passedNs / 1000000f;
            float speed = NEAnimationsLoader.config.animationSmoothingSpeed;

            interpolate(model.leftArm, last, 0, timePassed, differentFrame,
                    speed);
            interpolate(model.rightArm, last, 3, timePassed, differentFrame,
                    speed);
            interpolate(model.leftLeg, last, 6, timePassed, differentFrame,
                    speed);
            interpolate(model.rightLeg, last, 9, timePassed, differentFrame,
                    speed);
            // For now located here due to smoothing logic being here.
            if(NEAnimationsLoader.config.rotationLock == RotationLock.SMOOTH) {
                interpolateYawBodyHead(entity, last, 12, timePassed, differentFrame, 6);
            }
            data.setUpdated(frameId);
        }
    }

    private void lateBind() {
        NEAnimationsLoader.INSTANCE.animationProvider.refreshEnabledAnimations();
        doneLatebind = true;
    }
    
    public void nextFrame() {
        frameId++;
    }

    public void renderingFirstPersonArm(boolean flag) {
        this.renderingFirstPersonArm = flag;
    }

    private void interpolate(ModelPart model, float[] last, int offset, float timePassed, boolean differentFrame,
            float speed) {
        if (!differentFrame) { // Rerendering the place in the same frame
            model.xRot = (last[offset]);
            model.yRot = (last[offset + 1]);
            model.zRot = (last[offset + 2]);
            return;
        }
        if (timePassed > 200) { // Don't try to interpolate states older than 200ms
            last[offset] = model.xRot;
            last[offset + 1] = model.yRot;
            last[offset + 2] = model.zRot;
            cleanInvalidData(last, offset);
            return;
        }
        float amount = ((1f / (1000f / timePassed))) * speed;
        amount = Math.min(amount, 1);
        last[offset] = last[offset] + ((model.xRot - last[offset]) * amount);
        last[offset + 1] = last[offset + 1] + ((wrapDegrees(model.yRot) - wrapDegrees(last[offset + 1])) * amount);
        last[offset + 2] = last[offset + 2] + ((model.zRot - last[offset + 2]) * amount);
        cleanInvalidData(last, offset);
        model.xRot = (last[offset]);
        model.yRot = (last[offset + 1]);
        model.zRot = (last[offset + 2]);
    }
    
    private void interpolateYawBodyHead(AbstractClientPlayer entity, float[] last, int offset, float timePassed, boolean differentFrame,
            float speed) {
        if (!differentFrame) { // Rerendering the place in the same frame
            entity.yBodyRot = (last[offset]);
            entity.yBodyRotO = entity.yBodyRot;
            return;
        }
        if (timePassed > 200) { // Don't try to interpolate states older than 200ms
            last[offset] = entity.yHeadRot;
            return;
        }
        float amount = ((1f / (1000f / timePassed))) * speed;
        amount = Math.min(amount, 1);
        last[offset] += (entity.yHeadRot - last[offset]) * amount;
        entity.yBodyRot = (last[offset]);
        entity.yBodyRotO = entity.yBodyRot;
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

    private float wrapDegrees(float f) {
        float g = f % 6.28318512f;
        if (g >= 3.14159256f) {
            g -= 6.28318512f;
        }
        if (g < -3.14159256f) {
            g += 6.28318512f;
        }
        return g;
    }

}
