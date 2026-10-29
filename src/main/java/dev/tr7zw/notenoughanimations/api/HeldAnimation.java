package dev.tr7zw.notenoughanimations.api;

import com.mojang.blaze3d.vertex.*;
import lombok.*;
import net.minecraft.client.model.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

public interface HeldAnimation {

    void onLoad();

    /* return true when something got applied and any further processing gets canceled, hides the held item vanilla logic */
    boolean applyAnimation(HeldAnimationData data);

    @Getter
    @Setter
    class HeldAnimationData {
        LivingEntity entity;
        EntityModel<?> model;
        ItemStack itemStack;
        HumanoidArm arm;
        PoseStack matrices;
        //? if >= 1.21.9 {

        net.minecraft.client.renderer.SubmitNodeCollector vertexConsumers;
        net.minecraft.client.renderer.entity.state.LivingEntityRenderState livingEntityRenderState;
        //? } else {
        /*
         net.minecraft.client.renderer.MultiBufferSource vertexConsumers;
        *///? }
        int light;
    }

}
