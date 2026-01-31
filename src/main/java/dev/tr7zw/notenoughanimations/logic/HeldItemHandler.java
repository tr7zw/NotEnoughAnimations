package dev.tr7zw.notenoughanimations.logic;

import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.notenoughanimations.access.*;
import dev.tr7zw.notenoughanimations.versionless.animations.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.core.particles.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.util.MapRenderer;
import dev.tr7zw.notenoughanimations.util.NMSWrapper;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.transition.mc.GeneralUtil;
import dev.tr7zw.transition.mc.ItemUtil;
import dev.tr7zw.transition.mc.MathUtil;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

//? if >= 1.21.11 {

import net.minecraft.client.renderer.entity.state.*;
import org.joml.*;
//? }

public class HeldItemHandler implements DataHolder<HeldItemHandler.HeldItemState> {

    private Item filledMap = ItemUtil.getItem(GeneralUtil.getResourceLocation("minecraft", "filled_map"));
    private Set<Item> hideItemsForTheseBows = new HashSet<>();
    private Set<Item> lanternItems = new HashSet<>();

    public void onLoad() {
        hideItemsForTheseBows.clear();
        hideItemsForTheseBows.addAll(AnimationUtil.parseItemList(NEABaseMod.config.hideItemsForTheseBows));

        lanternItems.clear();
        lanternItems.addAll(AnimationUtil.parseItemList(NEABaseMod.config.lanternItems));
    }

    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices,
            //? if >= 1.21.9 {

            net.minecraft.client.renderer.SubmitNodeCollector vertexConsumers,
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState,
            //? } else {
            /*
                     net.minecraft.client.renderer.MultiBufferSource vertexConsumers, 
                    *///? }
            int light, CallbackInfo info) {
        if (entity == null) {
            // Can't really render an item if the entity doesn't exist can ya
            return;
        }
        if (entity.isSleeping()) { // Stop holding stuff in your sleep
            if (NEABaseMod.config.dontHoldItemsInBed) {
                info.cancel();
            }
            return;
        }
        if (NMSWrapper.hasCustomModel(itemStack)) {
            // Don't replace the model of items with a custom model
            return;
        }
        if (model instanceof ArmedModel armedModel && model instanceof HumanoidModel<?> humanoid) {
            if ((arm == HumanoidArm.RIGHT && humanoid.rightArm.visible)
                    || (arm == HumanoidArm.LEFT && humanoid.leftArm.visible)) {
                if (NEABaseMod.config.enableInWorldMapRendering) {
                    if (arm == entity.getMainArm() && entity.getMainHandItem().getItem().equals(filledMap)) { // Mainhand
                                                                                                              // with
                                                                                                              // or
                                                                                                              // without
                                                                                                              // the
                                                                                                              // offhand
                        matrices.pushPose();
                        //? if >= 1.21.9 {

                        armedModel.translateToHand(livingEntityRenderState, arm, matrices);
                        //? } else {
                        /*
                         armedModel.translateToHand(arm, matrices);
                        *///? }
                        matrices.mulPose(MathUtil.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(MathUtil.YP.rotationDegrees(205.0f));
                        matrices.mulPose(MathUtil.ZP.rotationDegrees(10.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((bl ? -1 : 1) / 16.0f, 0.09 + (entity.getOffhandItem().isEmpty() ? 0.15 : 0),
                                -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack,
                                !entity.getOffhandItem().isEmpty(), entity.getMainArm() == HumanoidArm.LEFT);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                    if (arm != entity.getMainArm() && entity.getOffhandItem().getItem().equals(filledMap)) { // Only
                                                                                                             // offhand
                        matrices.pushPose();
                        //? if >= 1.21.9 {

                        armedModel.translateToHand(livingEntityRenderState, arm, matrices);
                        //? } else {
                        /*
                         armedModel.translateToHand(arm, matrices);
                        *///? }
                        matrices.mulPose(MathUtil.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(MathUtil.YP.rotationDegrees(200.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((bl ? -1 : 1) / 16.0f, 0.125, -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack, true, false);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                }
                // Lantern animation
                //? if >= 1.21.11 {
                if (NEABaseMod.config.animateLanterns && entity instanceof PlayerData playerData
                        && lanternItems.contains(itemStack.getItem())) {
                    lanternAnimation(entity, playerData, itemStack, arm, matrices, vertexConsumers,
                            livingEntityRenderState, armedModel);
                    info.cancel();
                    return;
                }
                //? }
            }
        }

        if (NEABaseMod.config.enableOffhandHiding && entity instanceof AbstractClientPlayer player
                && !(player.getMainHandItem().getItem() instanceof ShieldItem)) {
            boolean mainHandProjectileWeapon = player.getMainHandItem().getItem() instanceof ProjectileWeaponItem;
            boolean offHandProjectileWeapon = player.getOffhandItem().getItem() instanceof ProjectileWeaponItem;
            if (!mainHandProjectileWeapon) {
                mainHandProjectileWeapon = hideItemsForTheseBows.contains(player.getMainHandItem().getItem());
            }
            if (!offHandProjectileWeapon) {
                offHandProjectileWeapon = hideItemsForTheseBows.contains(player.getOffhandItem().getItem());
            }
            boolean projectileWeaponEquipped = mainHandProjectileWeapon || offHandProjectileWeapon;
            boolean mainHandCharged = AnimationUtil.isChargedCrossbow(player.getMainHandItem());
            boolean offHandCharged = AnimationUtil.isChargedCrossbow(player.getOffhandItem());
            boolean isUsingItem = player.isUsingItem();
            if (!mainHandCharged && isUsingItem) {
                //? if >= 1.21.0 {

                mainHandCharged = ((float) (player.getMainHandItem().getUseDuration(player)
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getMainHandItem(), player) >= 1.0f);
                //? } else {
                /*
                 mainHandCharged = ((float) (player.getMainHandItem().getUseDuration()
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getMainHandItem()) >= 1.0f);
                *///? }
            }
            if (!offHandCharged && isUsingItem) {
                //? if >= 1.21.0 {

                offHandCharged = ((float) (player.getOffhandItem().getUseDuration(player)
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getOffhandItem(), player) >= 1.0f);
                //? } else {
                /*
                 offHandCharged = ((float) (player.getOffhandItem().getUseDuration()
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getOffhandItem()) >= 1.0f);
                *///? }
            }

            ArmPose mainHandPose = AnimationUtil.getArmPose(player, InteractionHand.MAIN_HAND);
            ArmPose offHandPose = AnimationUtil.getArmPose(player, InteractionHand.OFF_HAND);
            if (!(AnimationUtil.isUsingBothHands(mainHandPose) || AnimationUtil.isUsingBothHands(offHandPose)
                    || (projectileWeaponEquipped && (mainHandCharged || offHandCharged || isUsingItem))))
                return;

            if (mainHandPose.isTwoHanded()) {
                offHandPose = player.getOffhandItem().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            HumanoidArm mainArm = HumanoidArm.RIGHT;
            HumanoidArm offArm = HumanoidArm.LEFT;
            if (player.getMainArm() == HumanoidArm.LEFT) {
                mainArm = HumanoidArm.LEFT;
                offArm = HumanoidArm.RIGHT;
            }
            if (arm == mainArm && AnimationUtil.isUsingBothHands(offHandPose)) {
                info.cancel();
                return;
            } else if (arm == offArm && AnimationUtil.isUsingBothHands(mainHandPose)) {
                info.cancel();
                return;
            } else if (projectileWeaponEquipped && (mainHandCharged || offHandCharged || isUsingItem)
                    && !((mainHandCharged || offHandCharged) && isUsingItem) // yay, 1.21.1 is weird!! ~ EW
                    && !(AnimationUtil.isUsingBothHands(mainHandPose) || AnimationUtil.isUsingBothHands(offHandPose))) {
                // Some mods like Archer have non-vanilla friendly animation systems that don't trigger
                // our both hands check. So, we compromise. If all else fails, we do this hacky mess! - EW
                if (arm == mainArm && offHandProjectileWeapon && !mainHandProjectileWeapon) {
                    info.cancel();
                    return;
                } else if (arm == offArm && mainHandProjectileWeapon) {
                    info.cancel();
                    return;
                }
            }
        }
    }

    //? if >= 1.21.11 {
    private void lanternAnimation(LivingEntity entity, PlayerData playerData, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices, SubmitNodeCollector vertexConsumers, LivingEntityRenderState livingEntityRenderState,
            ArmedModel armedModel) {
        matrices.pushPose();
        //? if >= 1.21.9 {

        armedModel.translateToHand(livingEntityRenderState, arm, matrices);
        //? } else {
        /*
         armedModel.translateToHand(arm, matrices);
        *///? }

        // Scale down
        matrices.scale(0.6f, 0.6f, 0.6f);

        HeldItemState state = playerData.getData(this, () -> new HeldItemState(entity));

        float chainOffset = 0.5F;
        float chainYOffset = 0.6F;
        // Apply offsets
        matrices.translate(arm == HumanoidArm.LEFT ? -0.45 : -0.5, 0.4, -0.3);
        // Move pivot to top of chain
        matrices.translate(chainOffset, chainYOffset, chainOffset);

        // Calculate difference
        Vec3 camPos = Minecraft.getInstance().getEntityRenderDispatcher().camera.position();
        Vector4f origin = new Vector4f(0, 0, 0, 1f);
        origin.mul(matrices.last().pose());
        Vec3 curPos = camPos.add(origin.x, origin.y, origin.z);
        matrices.mulPose(MathUtil.XP.rotationDegrees(-90));

        float yawRad = entity.getYRot() * Mth.DEG_TO_RAD;
        float pitchRad = entity.getXRot() * Mth.DEG_TO_RAD;

        Vec3 forward = new Vec3(-Mth.sin(yawRad) * Mth.cos(pitchRad), -Mth.sin(pitchRad),
                Mth.cos(yawRad) * Mth.cos(pitchRad));

        Vec3 right = new Vec3(Mth.cos(yawRad), 0, Mth.sin(yawRad));

        float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        Vec3 lerpedVelocity = state.lastLanternVelocity
                .add(state.lanternVelocity.subtract(state.lastLanternVelocity).multiply(delta, delta, delta));

        double forwardVel = lerpedVelocity.dot(forward);
        double rightVel = lerpedVelocity.dot(right);
        float swingAngleX = Mth.clamp((float) forwardVel * 90f, -90f, 90f);
        float swingAngleZ = Mth.clamp((float) -rightVel * 90f, -90f, 90f);
        swingAngleX -= entity.getXRot() * 0.25f;

        double stiffness = 0.15;
        double damping = 0.95;

        Vec3 displacement = state.lanternPos.subtract(curPos);
        Vec3 acceleration = displacement.scale(-stiffness);

        if (entity.tickCount != state.lanternLastTick) {
            state.lanternLastTick = entity.tickCount;
            state.lastLanternVelocity = state.lanternVelocity;
            state.lanternVelocity = state.lanternVelocity.add(acceleration);
            state.lanternVelocity = state.lanternVelocity.scale(damping);
            state.lanternPos = state.lanternPos.add(state.lanternVelocity);
        }
        matrices.mulPose(MathUtil.XP.rotationDegrees(swingAngleX));
        matrices.mulPose(MathUtil.ZP.rotationDegrees(swingAngleZ));

        // Return pivot
        matrices.translate(-chainOffset, -chainYOffset, -chainOffset);

        vertexConsumers.submitBlock(matrices, Block.byItem(itemStack.getItem()).defaultBlockState(),
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        matrices.popPose();
    }
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
