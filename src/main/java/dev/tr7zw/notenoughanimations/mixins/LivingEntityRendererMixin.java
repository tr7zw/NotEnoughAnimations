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
//#if MC >= 12104
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
//#endif

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    //#if MC >= 12102
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void addEntityToRenderState(LivingEntity livingEntity,
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState, float f,
            CallbackInfo ci) {
        ((ExtendedLivingRenderState) livingEntityRenderState).setEntity(livingEntity);
        //#if MC >= 12104
        if (livingEntityRenderState instanceof ArmedEntityRenderState armed) {
            ((ExtendedItemStackRenderState) armed.leftHandItem)
                    .setItemStack(livingEntity.getItemHeldByArm(HumanoidArm.LEFT));
            ((ExtendedItemStackRenderState) armed.rightHandItem)
                    .setItemStack(livingEntity.getItemHeldByArm(HumanoidArm.RIGHT));
        }
        //#endif
    }
    //#endif
}
