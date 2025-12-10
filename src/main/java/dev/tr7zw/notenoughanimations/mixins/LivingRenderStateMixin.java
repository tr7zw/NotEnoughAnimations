package dev.tr7zw.notenoughanimations.mixins;

import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//? if >= 1.21.2 {

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

@Mixin(LivingEntityRenderState.class)
public class LivingRenderStateMixin implements ExtendedLivingRenderState {

    @Unique
    private LivingEntity entity;

    @Override
    public void setEntity(LivingEntity player) {
        this.entity = player;
    }

    @Override
    public LivingEntity getEntity() {
        return entity;
    }
}
//? } else {
/*
 import net.minecraft.client.Minecraft;
 @Mixin(Minecraft.class)
 public class LivingRenderStateMixin {}
*///? }
