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
//#if MC >= 12104
import net.minecraft.client.renderer.item.ItemStackRenderState;
import dev.tr7zw.notenoughanimations.access.ExtendedItemStackRenderState;
//#endif
//#if MC >= 12102
import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
import net.minecraft.client.model.ArmedModel;
//#if MC < 12104
//$$ import net.minecraft.client.resources.model.BakedModel;
//#endif
//#endif
//#if MC >= 11904
//#if MC < 12104
//$$import net.minecraft.world.item.ItemDisplayContext;
//#endif
//#else
//$$ import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
//#endif

@Mixin(ItemInHandLayer.class)
//#if MC >= 12109
public abstract class ItemInHandLayerMixin<S extends net.minecraft.client.renderer.entity.state.ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel>
        extends RenderLayer<S, M> {
    public ItemInHandLayerMixin(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }
    //#elseif MC >= 12102
    //$$public abstract class ItemInHandLayerMixin<S extends net.minecraft.client.renderer.entity.state.LivingEntityRenderState, M extends EntityModel<S> & ArmedModel>
    //$$        extends RenderLayer<S, M> {
    //$$    public ItemInHandLayerMixin(RenderLayerParent<S, M> renderer) {
    //$$       super(renderer);
    //$$   }
    //#else
    //$$public abstract class ItemInHandLayerMixin<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    //$$    public ItemInHandLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
    //$$        super(renderLayerParent);
    //$$    }
    //#endif

    //#if MC >= 12109
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
    //#else
    //$$@Inject(at = @At("HEAD"), method = "renderArmWithItem", cancellable = true)
    //#if MC >= 12104
    //$$private void renderArmWithItem(net.minecraft.client.renderer.entity.state.ArmedEntityRenderState livingEntityRenderState,
    //$$       ItemStackRenderState itemStackRenderState, HumanoidArm humanoidArm, PoseStack poseStack,
    //$$        net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    //$$    LivingEntity livingEntity = ((ExtendedLivingRenderState) livingEntityRenderState).getEntity();
    //$$    ItemStack itemStack = null;
    //$$    if (itemStackRenderState instanceof ExtendedItemStackRenderState ext && ext.getItemStack() != null) {
    //$$        itemStack = ext.getItemStack();
    //$$    } else {
    //$$        return;
    //$$    }
    //#elseif MC >= 12102
    //$$private void renderArmWithItem(net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState, BakedModel bakedModel,
    //$$        ItemStack itemStack, ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
    //$$        net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    //$$    LivingEntity livingEntity = ((ExtendedLivingRenderState) livingEntityRenderState).getEntity();
    //#elseif MC >= 11904
    //$$private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack,
    //$$      ItemDisplayContext itemDisplayContext, HumanoidArm humanoidArm, PoseStack poseStack,
    //$$      net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    //#else
    //$$ private void renderArmWithItem(LivingEntity livingEntity, ItemStack itemStack, TransformType transformType, HumanoidArm humanoidArm, PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
    //#endif
    //$$   if (livingEntity == null) {
    //$$        return;
    //$$    }
    //$$    NEAnimationsLoader.INSTANCE.heldItemHandler.onRenderItem(livingEntity, this.getParentModel(), itemStack,
    //$$            humanoidArm, poseStack, multiBufferSource, i, ci);
    //$$ }
    //#endif

}
