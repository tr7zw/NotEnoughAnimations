package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//#if MC >= 12105
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    //#if MC >= 12105
    @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
    protected void tickHeadTurn(float g, CallbackInfo info) {
        //#else
        //$$ @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
        //$$ protected void tickHeadTurn(float f, float g, CallbackInfoReturnable<Float> info) {
        //#endif
        if (this instanceof PlayerData) {
            PlayerData data = (PlayerData) this;
            if (data.isDisableBodyRotation()) {
                data.setDisableBodyRotation(false);
                //#if MC < 12105
                //$$ info.setReturnValue(g);
                //#endif
                info.cancel();
            }
        }
    }

}
