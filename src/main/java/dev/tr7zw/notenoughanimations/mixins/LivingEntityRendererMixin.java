package dev.tr7zw.notenoughanimations.mixins;

import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    //#if MC >= 12102
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void addEntityToRenderState(LivingEntity livingEntity,
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState, float f,
            CallbackInfo ci) {
        ((ExtendedLivingRenderState) livingEntityRenderState).setEntity(livingEntity);
    }
    //#endif
}
