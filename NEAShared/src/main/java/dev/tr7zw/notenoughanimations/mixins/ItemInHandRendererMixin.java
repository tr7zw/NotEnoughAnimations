package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.HumanoidArm;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "renderPlayerArm", at = @At("HEAD"))
    private void renderPlayerArm(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g,
            HumanoidArm humanoidArm, CallbackInfo info) {
        NEAnimationsLoader.INSTANCE.playerTransformer.renderingFirstPersonArm(true);
    }
    
    @Inject(method = "renderPlayerArm", at = @At("RETURN"))
    private void renderPlayerArmEnd(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, float g,
            HumanoidArm humanoidArm, CallbackInfo info) {
        NEAnimationsLoader.INSTANCE.playerTransformer.renderingFirstPersonArm(false);
    }
    
}
