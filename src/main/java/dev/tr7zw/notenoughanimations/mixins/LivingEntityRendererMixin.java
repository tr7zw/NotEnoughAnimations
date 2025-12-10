package dev.tr7zw.notenoughanimations.mixins;

import dev.tr7zw.notenoughanimations.access.ExtendedItemStackRenderState;
import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >= 1.21.4 {

import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
//? }

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    //? if >= 1.21.2 {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void addEntityToRenderState(LivingEntity livingEntity,
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState, float f,
            CallbackInfo ci) {
        ((ExtendedLivingRenderState) livingEntityRenderState).setEntity(livingEntity);
        //? if >= 1.21.11 {

        if (livingEntityRenderState instanceof ArmedEntityRenderState armed) {
            ((ExtendedItemStackRenderState) armed.leftHandItemState)
                    .setItemStack(livingEntity.getItemHeldByArm(HumanoidArm.LEFT));
            ((ExtendedItemStackRenderState) armed.rightHandItemState)
                    .setItemStack(livingEntity.getItemHeldByArm(HumanoidArm.RIGHT));
        }
        //? } else if >= 1.21.4 {
        /*
        if (livingEntityRenderState instanceof ArmedEntityRenderState armed) {
            ((ExtendedItemStackRenderState) armed.leftHandItem)
                    .setItemStack(livingEntity.getItemHeldByArm(HumanoidArm.LEFT));
            ((ExtendedItemStackRenderState) armed.rightHandItem)
                    .setItemStack(livingEntity.getItemHeldByArm(HumanoidArm.RIGHT));
        }
        *///? }
    }
    //? }
}
