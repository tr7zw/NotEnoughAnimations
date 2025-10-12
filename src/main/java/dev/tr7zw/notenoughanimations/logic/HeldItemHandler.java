package dev.tr7zw.notenoughanimations.logic;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.util.MapRenderer;
import dev.tr7zw.notenoughanimations.util.NMSWrapper;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.transition.mc.GeneralUtil;
import dev.tr7zw.transition.mc.ItemUtil;
import dev.tr7zw.transition.mc.MathUtil;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HeldItemHandler {

    private Item filledMap = ItemUtil.getItem(GeneralUtil.getResourceLocation("minecraft", "filled_map"));

    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices,
            //#if MC >= 12109
            net.minecraft.client.renderer.SubmitNodeCollector vertexConsumers,
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState,
            //#else
            //$$net.minecraft.client.renderer.MultiBufferSource vertexConsumers, 
            //#endif
            int light, CallbackInfo info) {
        if (entity.isSleeping()) { // Stop holding stuff in your sleep
            if (NEABaseMod.config.dontHoldItemsInBed) {
                info.cancel();
            }
            return;
        }
        if (NMSWrapper.hasCustomModel(itemStack)) {
            // Don't replace the model of items with a custom model
            return;
        }
        if (model instanceof ArmedModel armedModel && model instanceof HumanoidModel<?> humanoid) {
            if ((arm == HumanoidArm.RIGHT && humanoid.rightArm.visible)
                    || (arm == HumanoidArm.LEFT && humanoid.leftArm.visible)) {
                if (NEABaseMod.config.enableInWorldMapRendering) {
                    if (arm == entity.getMainArm() && entity.getMainHandItem().getItem().equals(filledMap)) { // Mainhand
                                                                                                              // with
                                                                                                              // or
                                                                                                              // without
                                                                                                              // the
                                                                                                              // offhand
                        matrices.pushPose();
                        //#if MC >= 12109
                        armedModel.translateToHand(livingEntityRenderState, arm, matrices);
                        //#else
                        //$$armedModel.translateToHand(arm, matrices);
                        //#endif
                        matrices.mulPose(MathUtil.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(MathUtil.YP.rotationDegrees(205.0f));
                        matrices.mulPose(MathUtil.ZP.rotationDegrees(10.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((bl ? -1 : 1) / 16.0f, 0.09 + (entity.getOffhandItem().isEmpty() ? 0.15 : 0),
                                -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack,
                                !entity.getOffhandItem().isEmpty(), entity.getMainArm() == HumanoidArm.LEFT);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                    if (arm != entity.getMainArm() && entity.getOffhandItem().getItem().equals(filledMap)) { // Only
                                                                                                             // offhand
                        matrices.pushPose();
                        //#if MC >= 12109
                        armedModel.translateToHand(livingEntityRenderState, arm, matrices);
                        //#else
                        //$$armedModel.translateToHand(arm, matrices);
                        //#endif
                        matrices.mulPose(MathUtil.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(MathUtil.YP.rotationDegrees(200.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((bl ? -1 : 1) / 16.0f, 0.125, -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack, true, false);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                }
            }
        }

        if (NEABaseMod.config.enableOffhandHiding && entity instanceof AbstractClientPlayer player) {
            ArmPose armPose = AnimationUtil.getArmPose(player, InteractionHand.MAIN_HAND);
            ArmPose armPose2 = AnimationUtil.getArmPose(player, InteractionHand.OFF_HAND);
            if (!(AnimationUtil.isUsingboothHands(armPose) || AnimationUtil.isUsingboothHands(armPose2)))
                return;
            if (armPose.isTwoHanded()) {
                armPose2 = player.getOffhandItem().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            if (player.getMainArm() == HumanoidArm.RIGHT) {
                if (arm == HumanoidArm.RIGHT && AnimationUtil.isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.LEFT && AnimationUtil.isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            } else {
                if (arm == HumanoidArm.LEFT && AnimationUtil.isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.RIGHT && AnimationUtil.isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            }
        }
    }

}
