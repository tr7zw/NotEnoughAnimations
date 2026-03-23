package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin(net.minecraft.client.renderer.entity.EntityRenderDispatcher.class)
public interface EntityRenderDispatcherAccessor {

    //? if >= 26.0 {

    @Accessor("blockModelResolver")
    net.minecraft.client.renderer.block.BlockModelResolver nea$getBlockModelResolver();
    //? }

}
