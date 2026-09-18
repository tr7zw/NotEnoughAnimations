package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import net.minecraft.world.entity.HumanoidArm;

//? if >= 26.3 {

@Mixin(net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer.class)
//? } else {
/*
@Mixin(net.minecraft.client.renderer.ItemInHandRenderer.class)
 */
//? }
public class ItemInHandRendererMixin {

    @Inject(method = "renderPlayerArm", at = @At("HEAD"))
    //? if >= 26.3 {
    private void renderPlayerArm(final PoseStack poseStack,
            final net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, final int lightCoords,
            final float inverseArmHeight, final float attackValue, final HumanoidArm arm,
            final net.minecraft.client.renderer.state.level.PlayerRenderState playerState, CallbackInfo info) {
        //? } else if >= 1.21.9 {
        /*
        private void renderPlayerArm(PoseStack poseStack,
            net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int i, float f, float g,
            HumanoidArm humanoidArm, CallbackInfo info) {
         */
        //? } else {
        /*
         private void renderPlayerArm(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int packedLight,
                float equippedProgress, float swingProgress, net.minecraft.world.entity.HumanoidArm humanoidArm, CallbackInfo info) {
        *///? }
        NEAnimationsLoader.INSTANCE.playerTransformer.renderingFirstPersonArm(true);
    }

    @Inject(method = "renderPlayerArm", at = @At("RETURN"))
    //? if >= 26.3 {
    private void renderPlayerArmEnd(final PoseStack poseStack,
            final net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, final int lightCoords,
            final float inverseArmHeight, final float attackValue, final HumanoidArm arm,
            final net.minecraft.client.renderer.state.level.PlayerRenderState playerState, CallbackInfo info) {
        //? } else if >= 1.21.9 {
        /*
        private void renderPlayerArmEnd(PoseStack poseStack,
            net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int i, float f, float g,
            HumanoidArm humanoidArm, CallbackInfo info) {
         */
        //? } else {
        /*
         private void renderPlayerArmEnd(PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int packedLight,
                float equippedProgress, float swingProgress, net.minecraft.world.entity.HumanoidArm humanoidArm, CallbackInfo info) {
        *///? }
        NEAnimationsLoader.INSTANCE.playerTransformer.renderingFirstPersonArm(false);
    }

}
