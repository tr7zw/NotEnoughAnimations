package dev.tr7zw.notenoughanimations.logic;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.util.MapRenderer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class HeldItemHandler {

    private Item filledMap = Registry.ITEM.get(new ResourceLocation("minecraft", "filled_map"));
    private Item crossbow = Registry.ITEM.get(new ResourceLocation("minecraft", "crossbow"));

    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
        if (entity.isSleeping()) { // Stop holding stuff in your sleep
            if(NEAnimationsLoader.config.dontHoldItemsInBed) {
                info.cancel();
            }
            return;
        }
        if (NEAnimationsLoader.config.enableInWorldMapRendering && model instanceof ArmedModel
                && NEAnimationsLoader.config.enableIngameMapHolding) {
            if (arm == entity.getMainArm() && entity.getMainHandItem().getItem().equals(filledMap)) { // Mainhand with
                                                                                                      // or without the
                                                                                                      // offhand
                matrices.pushPose();
                ((ArmedModel) model).translateToHand(arm, matrices);
                matrices.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
                matrices.mulPose(Vector3f.YP.rotationDegrees(200.0f));
                boolean bl = arm == HumanoidArm.LEFT;
                matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125, -0.625);
                MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack,
                        !entity.getOffhandItem().isEmpty(), entity.getMainArm() == HumanoidArm.LEFT);
                matrices.popPose();
                info.cancel();
                return;
            }
            if (arm != entity.getMainArm() && entity.getOffhandItem().getItem().equals(filledMap)) { // Only offhand
                matrices.pushPose();
                ((ArmedModel) model).translateToHand(arm, matrices);
                matrices.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
                matrices.mulPose(Vector3f.YP.rotationDegrees(200.0f));
                boolean bl = arm == HumanoidArm.LEFT;
                matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125, -0.625);
                MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack, true, false);
                matrices.popPose();
                info.cancel();
                return;
            }
        }

        if (NEAnimationsLoader.config.enableOffhandHiding && entity instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer) entity;
            ArmPose armPose = getArmPose(player, InteractionHand.MAIN_HAND);
            ArmPose armPose2 = getArmPose(player, InteractionHand.OFF_HAND);
            if (!(isUsingboothHands(armPose) || isUsingboothHands(armPose2)))
                return;
            if (armPose.isTwoHanded()) {
                armPose2 = player.getOffhandItem().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            if (player.getMainArm() == HumanoidArm.RIGHT) {
                if (arm == HumanoidArm.RIGHT && isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.LEFT && isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            } else {
                if (arm == HumanoidArm.LEFT && isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.RIGHT && isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            }
        }
    }

    private boolean isUsingboothHands(ArmPose pose) {
        return pose == ArmPose.BOW_AND_ARROW || pose == ArmPose.CROSSBOW_CHARGE || pose == ArmPose.CROSSBOW_HOLD;
    }

    private ArmPose getArmPose(AbstractClientPlayer abstractClientPlayerEntity, InteractionHand hand) {
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

    private boolean isChargedCrossbow(ItemStack item) {
        return CrossbowItem.isCharged(item);
    }

}
