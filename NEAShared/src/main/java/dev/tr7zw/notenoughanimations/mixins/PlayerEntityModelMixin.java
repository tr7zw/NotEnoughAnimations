package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(PlayerModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends HumanoidModel<T> {

	public PlayerEntityModelMixin() {
		super(null);
	}

	@SuppressWarnings("unchecked")
    @Inject(method = "setupAnim", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 0))
	public void setupAnim(T livingEntity, float f, float g, float tick, float i, float j, CallbackInfo info) {
	    if(livingEntity instanceof AbstractClientPlayer)
		    NEAnimationsLoader.INSTANCE.armTransformer.updateArms((AbstractClientPlayer) livingEntity, (PlayerModel<AbstractClientPlayer>)(Object)this, tick, f, info);
	}

}
