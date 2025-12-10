package dev.tr7zw.notenoughanimations.util;

import dev.tr7zw.notenoughanimations.versionless.animations.DataHolder;

public class RenderStateHolder implements DataHolder<RenderStateHolder.RenderStateData> {

    public static final RenderStateHolder INSTANCE = new RenderStateHolder();

    public static class RenderStateData {
        //? if >= 1.21.9 {

        public net.minecraft.client.renderer.entity.state.AvatarRenderState renderState;
        //? } else if >= 1.21.2 {
        /*
         public net.minecraft.client.renderer.entity.state.PlayerRenderState renderState;
        *///? }
    }
}
