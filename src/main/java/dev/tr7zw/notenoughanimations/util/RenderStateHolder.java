package dev.tr7zw.notenoughanimations.util;

import dev.tr7zw.notenoughanimations.versionless.animations.DataHolder;

public class RenderStateHolder implements DataHolder<RenderStateHolder.RenderStateData> {

    public static final RenderStateHolder INSTANCE = new RenderStateHolder();

    public static class RenderStateData {
        //#if MC >= 12102
        public net.minecraft.client.renderer.entity.state.PlayerRenderState renderState;
        //#endif
    }
}
