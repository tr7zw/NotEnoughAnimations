package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;

//spotless:off
//#if MC <= 12004
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif
//#if MC >= 11903
import org.joml.Matrix4f;
//#else
//$$ import com.mojang.math.Matrix4f;
//#endif
//spotless:on

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderLevel", at = @At("HEAD"))
    // spotless:off
  //#if MC <= 12004
  //$$  private void beforeRender(PoseStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline,
  //$$          Camera camera, GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f,
  //#else
    private void beforeRender(float tickDelta, long l, boolean bl, Camera camera, GameRenderer gameRenderer,
            LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2,
  //#endif
  //spotless:on
            CallbackInfo ci) {
        NEAnimationsLoader.INSTANCE.playerTransformer.setDeltaTick(tickDelta);
//        NEAnimationsLoader.INSTANCE.playerTransformer.nextFrame();
    }

}
