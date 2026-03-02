package dev.tr7zw.notenoughanimations.util;

import dev.tr7zw.notenoughanimations.versionless.animations.*;
import net.minecraft.client.player.*;
import net.minecraft.world.*;
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

    public static BodyPart getArm(AbstractClientPlayer entity, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return entity.getMainArm() == HumanoidArm.RIGHT ? BodyPart.RIGHT_ARM : BodyPart.LEFT_ARM;
        } else {
            return entity.getMainArm() == HumanoidArm.RIGHT ? BodyPart.LEFT_ARM : BodyPart.RIGHT_ARM;
        }
    }

}
