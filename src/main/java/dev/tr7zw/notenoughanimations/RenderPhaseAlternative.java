package dev.tr7zw.notenoughanimations;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.RenderPhase.Alpha;
import net.minecraft.client.render.RenderPhase.Cull;
import net.minecraft.client.render.RenderPhase.Lightmap;
import net.minecraft.client.render.RenderPhase.Transparency;

public class RenderPhaseAlternative {

	public static final Cull DISABLE_CULLING = new Cull(false);

	public static final Lightmap ENABLE_LIGHTMAP = new Lightmap(true);

	public static final Transparency TRANSLUCENT_TRANSPARENCY = new Transparency("translucent_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate((GlStateManager.SrcFactor) GlStateManager.SrcFactor.SRC_ALPHA,
				(GlStateManager.DstFactor) GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA,
				(GlStateManager.SrcFactor) GlStateManager.SrcFactor.ONE,
				(GlStateManager.DstFactor) GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
	}, () -> {
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	});

	public static final Alpha ONE_TENTH_ALPHA = new Alpha(0.003921569f);

	
}
