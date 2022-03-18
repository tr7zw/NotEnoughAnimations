package dev.tr7zw.notenoughanimations.logic;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.util.MapRenderer;
import dev.tr7zw.notenoughanimations.util.VanillaAnimationUtil;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HeldItemHandler {

    private Item filledMap = Registry.ITEM.get(new ResourceLocation("minecraft", "filled_map"));

    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
        if (entity.isSleeping()) { // Stop holding stuff in your sleep
            if(NEAnimationsLoader.config.dontHoldItemsInBed) {
                info.cancel();
            }
            return;
        }
        if (NEAnimationsLoader.config.enableInWorldMapRendering && model instanceof ArmedModel) {
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
            ArmPose armPose = VanillaAnimationUtil.getArmPose(player, InteractionHand.MAIN_HAND);
            ArmPose armPose2 = VanillaAnimationUtil.getArmPose(player, InteractionHand.OFF_HAND);
            if (!(VanillaAnimationUtil.isUsingboothHands(armPose) || VanillaAnimationUtil.isUsingboothHands(armPose2)))
                return;
            if (armPose.isTwoHanded()) {
                armPose2 = player.getOffhandItem().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            if (player.getMainArm() == HumanoidArm.RIGHT) {
                if (arm == HumanoidArm.RIGHT && VanillaAnimationUtil.isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.LEFT && VanillaAnimationUtil.isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            } else {
                if (arm == HumanoidArm.LEFT && VanillaAnimationUtil.isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.RIGHT && VanillaAnimationUtil.isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            }
        }
    }

}
