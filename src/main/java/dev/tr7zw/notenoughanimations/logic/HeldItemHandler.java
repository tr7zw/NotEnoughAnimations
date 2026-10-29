package dev.tr7zw.notenoughanimations.logic;

import com.mojang.blaze3d.vertex.*;
import dev.tr7zw.notenoughanimations.animations.held.*;
import dev.tr7zw.notenoughanimations.api.*;
import dev.tr7zw.notenoughanimations.util.*;
import dev.tr7zw.notenoughanimations.versionless.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;

//? if >= 1.21.11 {

import net.minecraft.client.renderer.entity.state.*;
import org.joml.*;
//? }

public class HeldItemHandler {

    private final List<HeldAnimation> heldAnimations = Arrays.asList(new OffhandHidingAnimation(), new MapAnimation(),
            new LanternAnimation());

    public void onLoad() {
        for (HeldAnimation animation : heldAnimations) {
            animation.onLoad();
        }
    }

    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices,
            //? if >= 1.21.9 {

            net.minecraft.client.renderer.SubmitNodeCollector vertexConsumers,
            net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState,
            //? } else {
            /*
                     net.minecraft.client.renderer.MultiBufferSource vertexConsumers, 
                    *///? }
            int light, CallbackInfo info) {
        if (entity == null) {
            // Can't really render an item if the entity doesn't exist can ya
            return;
        }
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

        var data = new HeldAnimation.HeldAnimationData();
        data.setItemStack(itemStack);
        data.setEntity(entity);
        data.setModel(model);
        data.setArm(arm);
        data.setMatrices(matrices);
        data.setVertexConsumers(vertexConsumers);
        data.setLight(light);
        //? if >= 1.21.9 {
        data.setLivingEntityRenderState(livingEntityRenderState);
        //? }
        for (HeldAnimation animation : heldAnimations) {
            if (animation.applyAnimation(data)) {
                info.cancel();
                return;
            }
        }

    }

}
