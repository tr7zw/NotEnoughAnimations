package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
//? if >= 1.21.5 {

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? } else {
/*
 import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///? }

import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    //? if >= 1.21.5 {

    @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
    protected void tickHeadTurn(float g, CallbackInfo info) {
        //? } else {
        /*
         @Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
         protected void tickHeadTurn(float f, float g, CallbackInfoReturnable<Float> info) {
        *///? }
        if (this instanceof PlayerData) {
            PlayerData data = (PlayerData) this;
            if (data.isDisableBodyRotation()) {
                data.setDisableBodyRotation(false);
                //? if < 1.21.5 {
                /*
                 info.setReturnValue(g);
                *///? }
                info.cancel();
            }
        }
    }

}
