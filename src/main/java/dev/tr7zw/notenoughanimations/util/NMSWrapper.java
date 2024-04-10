package dev.tr7zw.notenoughanimations.util;

import net.minecraft.world.item.ItemStack;

public class NMSWrapper {

    public static boolean hasCustomModel(ItemStack itemStack) {
        // spotless:off
        //#if MC <= 12004
        //$$ return itemStack.hasTag() && itemStack.getTag().contains("CustomModelData");
        //#else
        return itemStack.getComponents().has(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA);
        //#endif
        //spotless:on
    }

}
