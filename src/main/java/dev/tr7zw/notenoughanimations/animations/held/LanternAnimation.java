package dev.tr7zw.notenoughanimations.animations.held;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.notenoughanimations.access.*;
import dev.tr7zw.notenoughanimations.api.*;
import dev.tr7zw.notenoughanimations.logic.*;
import dev.tr7zw.notenoughanimations.mixins.*;
import dev.tr7zw.notenoughanimations.util.*;
import dev.tr7zw.notenoughanimations.versionless.*;
import dev.tr7zw.notenoughanimations.versionless.animations.*;
import dev.tr7zw.transition.mc.*;
import lombok.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

import java.util.*;

//? if >= 1.21.11 {

import net.minecraft.client.renderer.entity.state.*;
//? }

public class LanternAnimation implements DataHolder<LanternAnimation.HeldItemState>, HeldAnimation {

    private Set<Item> lanternItems = new HashSet<>();

    @Override
    public void onLoad() {
        lanternItems.clear();
        lanternItems.addAll(AnimationUtil.parseItemList(NEABaseMod.config.lanternItems));
    }

    //? if >= 1.21.11 {
    @Override
    public boolean applyAnimation(HeldAnimationData data) {
        if (NEABaseMod.config.animateLanterns && data.getEntity() instanceof PlayerData playerData
                && lanternItems.contains(data.getItemStack().getItem())
                && data.getModel() instanceof ArmedModel armedModel) {
            lanternAnimation(data.getEntity(), playerData, data.getItemStack(), data.getArm(), data.getMatrices(),
                    data.getVertexConsumers(), data.getLivingEntityRenderState(), armedModel);
            return true;
        }
        return false;
    }

    private void lanternAnimation(LivingEntity entity, PlayerData playerData, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices, SubmitNodeCollector vertexConsumers, LivingEntityRenderState livingEntityRenderState,
            ArmedModel armedModel) {
        matrices.pushPose();

        Vec3 matrixPos = MathUtil.getWorldSpacePosition(matrices.last().pose());
        Vec3 cameraPos = GeneralUtil.getCameraPos();

        //? if >= 1.21.9 {

        armedModel.translateToHand(livingEntityRenderState, arm, matrices);
        //? } else {
        /*
         armedModel.translateToHand(arm, matrices);
        *///? }

        HeldItemState state = playerData.getData(this, () -> new HeldItemState(entity));

        if (matrixPos.distanceToSqr(cameraPos) < 20 * 20 || entity.tickCount != state.lanternLastTick) {
            // only update the data if the matrix nearby the camera pos, not a far away entity, some hud rendering or shader pass
            // alternativly, when the entity is rendering but no valid matrix is found over the ticks, then use whatever we got
            updateData(entity, matrices, state);
        }

        // Scale down
        matrices.scale(0.6f, 0.6f, 0.6f);
        float chainOffset = 0.5F;
        float chainYOffset = 0.6F;
        // Apply offsets
        matrices.translate(arm == HumanoidArm.LEFT ? -0.45 : -0.5, 0.4, -0.3);
        // Move pivot to top of chain
        matrices.translate(chainOffset, chainYOffset, chainOffset);

        //? if >= 26.3 {

        matrices.rotate(MathUtil.XP.rotationDegrees(-90));
        //? } else {
        /*
        matrices.mulPose(MathUtil.XP.rotationDegrees(-90));
         */
        //? }

        // calculate the actual swing rotations
        float yawRad = entity.getYRot() * Mth.DEG_TO_RAD;
        float pitchRad = entity.getXRot() * Mth.DEG_TO_RAD;

        Vec3 forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad)).normalize();
        Vec3 right = new Vec3(forward.z, 0, -forward.x);

        float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        Vec3 lerpedVelocity = state.lastLanternVelocity
                .add(state.lanternVelocity.subtract(state.lastLanternVelocity).multiply(delta, delta, delta));

        double forwardVel = lerpedVelocity.dot(forward);
        double rightVel = lerpedVelocity.dot(right);

        float swingAngleX = Mth.clamp((float) forwardVel * 90f, -90f, 90f);
        float swingAngleZ = Mth.clamp((float) -rightVel * 90f, -90f, 90f);
        swingAngleX -= entity.getXRot() * 0.25f;

        //? if >= 26.3 {

        matrices.rotate(MathUtil.XP.rotationDegrees(swingAngleX));
        matrices.rotate(MathUtil.ZP.rotationDegrees(swingAngleZ));
        //? } else {
        /*
        matrices.mulPose(MathUtil.XP.rotationDegrees(swingAngleX));
        matrices.mulPose(MathUtil.ZP.rotationDegrees(swingAngleZ));
         */
        //? }

        // Return pivot
        matrices.translate(-chainOffset, -chainYOffset, -chainOffset);

        // the 26.1+ version is technically not correct for how stuff should be done
        //? if >= 26.0 {

        var blockState = new net.minecraft.client.renderer.block.BlockModelRenderState();
        ((EntityRenderDispatcherAccessor) Minecraft.getInstance().getEntityRenderDispatcher())
                .nea$getBlockModelResolver().update(blockState, Block.byItem(itemStack.getItem()).defaultBlockState(),
                        net.minecraft.client.renderer.block.model.BlockDisplayContext.create());
        blockState.submit(matrices, vertexConsumers, 15728880, OverlayTexture.NO_OVERLAY, 0);
        //? } else {
        /*
        vertexConsumers.submitBlock(matrices, Block.byItem(itemStack.getItem()).defaultBlockState(),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        *///? }
        matrices.popPose();
    }

    private static void updateData(LivingEntity entity, PoseStack matrices, HeldItemState state) {
        Vec3 rawCurPos = MathUtil.getWorldSpacePosition(matrices.last().pose());

        if (rawCurPos.distanceTo(state.lanternPos) > 10) {
            state.lanternPos = rawCurPos;
        }

        Vec3 curPos = rawCurPos;

        double stiffness = 0.15;
        double damping = 0.95;

        Vec3 displacement = state.lanternPos.subtract(curPos);
        // add Y movement influence, so the lantern moves when jumping/falling straight, rotated to the player
        var yawRad = entity.getYRot() * Mth.DEG_TO_RAD;
        var forward = new Vec3(-Mth.sin(yawRad), 0, Mth.cos(yawRad)).normalize();
        var right = new Vec3(forward.z, 0, -forward.x);
        displacement = displacement.add(displacement.y() * 0.1 * right.x, 0, displacement.y() * 0.1 * right.z);
        Vec3 acceleration = displacement.scale(-stiffness);

        if (entity.tickCount != state.lanternLastTick) {
            state.lanternLastTick = entity.tickCount;

            // Reset if too far
            if (displacement.lengthSqr() > 10) {
                state.lastLanternVelocity = state.lanternVelocity;
                state.lanternVelocity = acceleration;
                state.lanternPos = curPos;
            } else {
                // Standard physics update (full tick, no delta!)
                state.lastLanternVelocity = state.lanternVelocity;
                state.lanternVelocity = state.lanternVelocity.add(acceleration);
                state.lanternVelocity = state.lanternVelocity.scale(damping);
                state.lanternPos = state.lanternPos.add(state.lanternVelocity);
            }
        }
    }

    //? } else {
    /*
    @Override
    public boolean applyAnimation(HeldAnimationData data) {
        return false;
    }
     */
    //? }

    public static class HeldItemState {
        public int lanternLastTick = 0;
        public Vec3 lanternPos;
        public Vec3 lanternVelocity = Vec3.ZERO;
        public Vec3 lastLanternVelocity = Vec3.ZERO;

        public HeldItemState(LivingEntity entity) {
            lanternLastTick = entity.tickCount;
            lanternPos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        }
    }

}
