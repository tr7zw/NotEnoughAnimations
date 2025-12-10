package dev.tr7zw.notenoughanimations.util;

import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;

public class NMSWrapper {

    public static boolean hasCustomModel(ItemStack itemStack) {
        //? if <= 1.20.4 {
        /*
         return itemStack.hasTag() && itemStack.getTag().contains("CustomModelData");
        *///? } else {

        return itemStack.getComponents().has(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA);
        //? }
    }

    public static boolean onGround(Entity entity) {
        //? if >= 1.20.0 {

        return entity.onGround();
        //? } else {
        /*
        return entity.isOnGround();
        *///? }
    }

}
