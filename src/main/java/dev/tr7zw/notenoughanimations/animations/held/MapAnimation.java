package dev.tr7zw.notenoughanimations.animations.held;

import dev.tr7zw.notenoughanimations.api.*;
import dev.tr7zw.notenoughanimations.util.*;
import dev.tr7zw.notenoughanimations.versionless.*;
import dev.tr7zw.transition.mc.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

public class MapAnimation implements HeldAnimation {

    @Override
    public void onLoad() {

    }

    @Override
    public boolean applyAnimation(HeldAnimationData data) {
        if (NEABaseMod.config.enableInWorldMapRendering && data.getModel() instanceof ArmedModel armedModel
                && data.getModel() instanceof HumanoidModel<?> humanoid) {
            if ((data.getArm() == HumanoidArm.RIGHT && humanoid.rightArm.visible)
                    || (data.getArm() == HumanoidArm.LEFT && humanoid.leftArm.visible)) {
                var matrices = data.getMatrices();
                // Mainhand with or without the offhand
                if (data.getArm() == data.getEntity().getMainArm()
                        && data.getEntity().getMainHandItem().getItem() instanceof MapItem) {
                    matrices.pushPose();
                    //? if >= 1.21.9 {

                    armedModel.translateToHand(data.getLivingEntityRenderState(), data.getArm(), matrices);
                    //? } else {
                    /*
                     armedModel.translateToHand(data.getArm(), matrices);
                    *///? }
                       //? if >= 26.3 {

                    matrices.rotate(MathUtil.XP.rotationDegrees(-90.0f));
                    matrices.rotate(MathUtil.YP.rotationDegrees(205.0f));
                    matrices.rotate(MathUtil.ZP.rotationDegrees(10.0f));
                    //? } else {
                    /*
                    matrices.mulPose(MathUtil.XP.rotationDegrees(-90.0f));
                    matrices.mulPose(MathUtil.YP.rotationDegrees(205.0f));
                    matrices.mulPose(MathUtil.ZP.rotationDegrees(10.0f));
                     */
                    //? }
                    boolean bl = data.getArm() == HumanoidArm.LEFT;
                    matrices.translate((bl ? -1 : 1) / 16.0f,
                            0.09 + (data.getEntity().getOffhandItem().isEmpty() ? 0.15 : 0), -0.625);
                    MapRenderer.renderFirstPersonMap(matrices, data.getVertexConsumers(), data.getLight(),
                            data.getItemStack(), !data.getEntity().getOffhandItem().isEmpty(),
                            data.getEntity().getMainArm() == HumanoidArm.LEFT);
                    matrices.popPose();
                    return true;
                }
                // Only offhand
                if (data.getArm() != data.getEntity().getMainArm()
                        && data.getEntity().getOffhandItem().getItem() instanceof MapItem) {
                    matrices.pushPose();
                    //? if >= 1.21.9 {

                    armedModel.translateToHand(data.getLivingEntityRenderState(), data.getArm(), matrices);
                    //? } else {
                    /*
                     armedModel.translateToHand(data.getArm(), matrices);
                    *///? }
                       //? if >= 26.3 {

                    matrices.rotate(MathUtil.XP.rotationDegrees(-90.0f));
                    matrices.rotate(MathUtil.YP.rotationDegrees(200.0f));
                    //? } else {
                    /*
                    matrices.mulPose(MathUtil.XP.rotationDegrees(-90.0f));
                    matrices.mulPose(MathUtil.YP.rotationDegrees(200.0f));
                     */
                    //? }
                    boolean bl = data.getArm() == HumanoidArm.LEFT;
                    matrices.translate((bl ? -1 : 1) / 16.0f, 0.125, -0.625);
                    MapRenderer.renderFirstPersonMap(matrices, data.getVertexConsumers(), data.getLight(),
                            data.getItemStack(), true, false);
                    matrices.popPose();
                    return true;
                }
            }
        }
        return false;
    }

}
