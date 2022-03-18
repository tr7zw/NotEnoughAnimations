package dev.tr7zw.notenoughanimations.util;

import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class VanillaAnimationUtil {

    private static Item crossbow = Registry.ITEM.get(new ResourceLocation("minecraft", "crossbow"));
    
    public static boolean isUsingboothHands(ArmPose pose) {
        return pose == ArmPose.BOW_AND_ARROW || pose == ArmPose.CROSSBOW_CHARGE || pose == ArmPose.CROSSBOW_HOLD;
    }
    
    public static ArmPose getArmPose(AbstractClientPlayer abstractClientPlayerEntity, InteractionHand hand) {
        ItemStack itemStack = abstractClientPlayerEntity.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            return ArmPose.EMPTY;
        } else {
            if (abstractClientPlayerEntity.getUsedItemHand() == hand
                    && abstractClientPlayerEntity.getUseItemRemainingTicks() > 0) {
                UseAnim useAction = itemStack.getUseAnimation();
                if (useAction == UseAnim.BLOCK) {
                    return ArmPose.BLOCK;
                }

                if (useAction == UseAnim.BOW) {
                    return ArmPose.BOW_AND_ARROW;
                }

                if (useAction == UseAnim.SPEAR) {
                    return ArmPose.THROW_SPEAR;
                }
                
                if (useAction == UseAnim.SPYGLASS) {
                    return ArmPose.SPYGLASS;
                }

                if (useAction == UseAnim.CROSSBOW && hand.equals(abstractClientPlayerEntity.getUsedItemHand())) {
                    return ArmPose.CROSSBOW_CHARGE;
                }
            } else if (!abstractClientPlayerEntity.swinging && itemStack.getItem().equals(crossbow)
                    && isChargedCrossbow(itemStack)) {
                return ArmPose.CROSSBOW_HOLD;
            }

            return ArmPose.ITEM;
        }
    }
    
    public static boolean isChargedCrossbow(ItemStack item) {
        return CrossbowItem.isCharged(item);
    }
    
}