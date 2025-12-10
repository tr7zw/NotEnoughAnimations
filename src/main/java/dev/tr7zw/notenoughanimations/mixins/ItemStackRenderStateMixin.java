package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;

import dev.tr7zw.notenoughanimations.access.ExtendedItemStackRenderState;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.ItemStack;
//? if >= 1.21.4 {

import net.minecraft.client.renderer.item.ItemStackRenderState;

@Mixin(ItemStackRenderState.class)
public class ItemStackRenderStateMixin implements ExtendedItemStackRenderState {

    @Getter
    @Setter
    private ItemStack itemStack = null;

}
//? } else {
/*
 @Mixin(ItemStack.class)
 public class ItemStackRenderStateMixin {}
*///? }
