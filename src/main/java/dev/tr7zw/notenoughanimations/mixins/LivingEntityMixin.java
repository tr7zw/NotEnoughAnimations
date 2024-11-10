package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
    protected void tickHeadTurn(float f, float g, CallbackInfoReturnable<Float> info) {
        if (this instanceof PlayerData) {
            PlayerData data = (PlayerData) this;
            if (data.isDisableBodyRotation()) {
                data.setDisableBodyRotation(false);
                info.setReturnValue(g);
                info.cancel();
                return;
            }
        }
    }

}
