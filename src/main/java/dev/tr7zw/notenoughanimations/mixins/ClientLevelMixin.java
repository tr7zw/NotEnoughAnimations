package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsMod;
import net.minecraft.client.multiplayer.ClientLevel;

// Port of the Fabric mixin to (Neo)Forge, as the (Neo)Forge events do weird stuff
@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(method = "tickEntities", at = @At("HEAD"))
    private void startWorldTick(CallbackInfo ci) {
        NEAnimationsMod.INSTANCE.clientTick();
    }

}
