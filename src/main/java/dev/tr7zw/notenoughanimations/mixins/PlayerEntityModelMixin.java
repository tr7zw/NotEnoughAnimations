package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(PlayerModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends HumanoidModel<T> {

    public PlayerEntityModelMixin() {
        // spotless:off
    	//#if MC >= 11700
    	super(null);
		//#else
		//$$ super(0);
		//#endif
		//spotless:on

    }

    @SuppressWarnings("unchecked")
    @Inject(method = "setupAnim", at = @At(value = "HEAD"))
    public void setupAnimHEAD(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch, CallbackInfo info) {
        if (livingEntity instanceof AbstractClientPlayer)
            NEAnimationsLoader.INSTANCE.playerTransformer.preUpdate((AbstractClientPlayer) livingEntity,
                    (PlayerModel<AbstractClientPlayer>) (Object) this, limbSwing, info);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "setupAnim", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 0))
    public void setupAnim(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, CallbackInfo info) {
        if (livingEntity instanceof AbstractClientPlayer)
            NEAnimationsLoader.INSTANCE.playerTransformer.updateModel((AbstractClientPlayer) livingEntity,
                    (PlayerModel<AbstractClientPlayer>) (Object) this, limbSwing, info);
    }

    @Inject(method = "setupAnim", at = @At(value = "RETURN"))
    public void setupAnimEnd(T livingEntity, float swing, float g, float tick, float i, float j, CallbackInfo info) {
        if (livingEntity instanceof PlayerData) {
            PlayerData data = (PlayerData) livingEntity;
            AbstractClientPlayer player = (AbstractClientPlayer) livingEntity;
            if (data.getPoseOverwrite() != null) {
                player.setPose(data.getPoseOverwrite());
                data.setPoseOverwrite(null);
            }
        }
    }

}
