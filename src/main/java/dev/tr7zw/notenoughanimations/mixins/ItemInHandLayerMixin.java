package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
//? if >= 1.21.4 {

import net.minecraft.client.renderer.item.ItemStackRenderState;
import dev.tr7zw.notenoughanimations.access.ExtendedItemStackRenderState;
//? }
//? if >= 1.21.2 {

import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
import net.minecraft.client.model.ArmedModel;
//? if < 1.21.4 {
/*
 import net.minecraft.client.resources.model.BakedModel;
*///? }
   //? }
   //? if >= 1.19.4 {

//? if < 1.21.4 {
/*
 import net.minecraft.world.item.ItemDisplayContext;
*///? }
   //? } else {
   /*
    import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
   *///? }

@Mixin(ItemInHandLayer.class)
//? if >= 1.21.9 {

public abstract class ItemInHandLayerMixin<S extends net.minecraft.client.renderer.entity.state.ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel>
        extends RenderLayer<S, M> {
    public ItemInHandLayerMixin(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }
    //? } else if >= 1.21.2 {
/*
     public abstract class ItemInHandLayerMixin<S extends net.minecraft.client.renderer.entity.state.LivingEntityRenderState, M extends EntityModel<S> & ArmedModel>
            extends RenderLayer<S, M> {
        public ItemInHandLayerMixin(RenderLayerParent<S, M> renderer) {
           super(renderer);
       }
    *///? } else {
    /*
     public abstract class ItemInHandLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
        public ItemInHandLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
            super(renderLayerParent);
        }
    *///? }

    //? if >= 1.21.9 {

    @Inject(at = @At("HEAD"), method = "submitArmWithItem", cancellable = true)
    private void submitArmWithItem(S armedEntityRenderState, ItemStackRenderState itemStackRenderState,
            HumanoidArm humanoidArm, PoseStack poseStack,
            net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector, int i, CallbackInfo ci) {
        LivingEntity livingEntity = ((ExtendedLivingRenderState) armedEntityRenderState).getEntity();
        ItemStack itemStack = null;
        if (itemStackRenderState instanceof ExtendedItemStackRenderState ext && ext.getItemStack() != null) {
            itemStack = ext.getItemStack();
        } else {
            return;
        }
        NEAnimationsLoader.INSTANCE.heldItemHandler.onRenderItem(livingEntity, this.getParentModel(), itemStack,
                humanoidArm, poseStack, submitNodeCollector, armedEntityRenderState, i, ci);
    }
    //? } else {
    /*
     @Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
     //? if >= 1.21.4 {
    
      private void renderArmWithItem(net.minecraft.client.renderer.entity.state.ArmedEntityRenderState livingEntityRenderState,
            ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack,
             net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
         LivingEntity livingEntity = ((ExtendedLivingRenderState) livingEntityRenderState).getEntity();
         ItemStack itemStack = null;
         if (itemStackRenderState instanceof ExtendedItemStackRenderState ext && ext.getItemStack() != null) {
             itemStack = ext.getItemStack();
         } else {
             return;
         }
     //? } else if >= 1.21.2 {
    /^
      private void renderArmWithItem(net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState, BakedModel bakedModel,
             ItemStack itemStack, ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
             net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
         LivingEntity livingEntity = ((ExtendedLivingRenderState) livingEntityRenderState).getEntity();
     ^///? } else if >= 1.19.4 {
    /^
      private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack,
           ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
           net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
     ^///? } else {
    /^
      private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, TransformType transformType, HumanoidArm humanoidArm, PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
     ^///? }
       if (livingEntity == null) {
            return;
        }
        NEAnimationsLoader.INSTANCE.heldItemHandler.onRenderItem(livingEntity, this.getParentModel(), itemStack,
                humanoidArm, poseStack, multiBufferSource, i, ci);
     }
    *///? }

}
