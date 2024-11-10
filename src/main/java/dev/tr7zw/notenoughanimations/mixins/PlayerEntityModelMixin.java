package dev.tr7zw.notenoughanimations.mixins;

//#if MC >= 12102
import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

@Mixin(PlayerModel.class)
//#if MC >= 12102
public abstract class PlayerEntityModelMixin extends HumanoidModel<PlayerRenderState> {
    //#else
    //$$public abstract class PlayerEntityModelMixin<T extends net.minecraft.world.entity.LivingEntity> extends HumanoidModel<T> {
    //#endif

    @Unique
    //#if MC >= 12102
    private static final String SETUP_ANIM_METHOD = "setupAnim(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;)V";
    //#else
    //$$private static final String SETUP_ANIM_METHOD = "setupAnim";
    //#endif

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
    @Inject(method = SETUP_ANIM_METHOD, at = @At(value = "HEAD"))
    //#if MC >= 12102
    public void setupAnimHEAD(PlayerRenderState state, CallbackInfo info) {
        float limbSwing = state.walkAnimationPos; // makes total sense :thumbs_up:
        PlayerModel model = (PlayerModel) (Object) this;
        AbstractClientPlayer player = (AbstractClientPlayer) ((ExtendedLivingRenderState) state).getEntity();
        //#else
        //$$public void setupAnimHEAD(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
        //$$        float netHeadYaw, float headPitch, CallbackInfo info) {
        //$$    PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) (Object) this;
        //$$    if (!(livingEntity instanceof AbstractClientPlayer)) return;
        //$$    AbstractClientPlayer player = (AbstractClientPlayer) livingEntity;
        //#endif
        NEAnimationsLoader.INSTANCE.playerTransformer.preUpdate(player, model, limbSwing, info);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = SETUP_ANIM_METHOD, at = @At(value = "RETURN"))
    //#if MC >= 12102
    public void setupAnim(PlayerRenderState state, CallbackInfo info) {
        float limbSwing = state.walkAnimationPos; // makes total sense :thumbs_up:
        PlayerModel model = (PlayerModel) (Object) this;
        AbstractClientPlayer player = (AbstractClientPlayer) ((ExtendedLivingRenderState) state).getEntity();
        //#else
        //$$public void setupAnim(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
        //$$        float netHeadYaw, float headPitch, CallbackInfo info) {
        //$$    PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) (Object) this;
        //$$    if (!(livingEntity instanceof AbstractClientPlayer)) return;
        //$$    AbstractClientPlayer player = (AbstractClientPlayer) livingEntity;
        //#endif
        NEAnimationsLoader.INSTANCE.playerTransformer.updateModel(player, model, limbSwing, info);
    }

    @Inject(method = SETUP_ANIM_METHOD, at = @At(value = "RETURN"))
    //#if MC >= 12102
    public void setupAnimEnd(PlayerRenderState state, CallbackInfo info) {
        AbstractClientPlayer player = (AbstractClientPlayer) ((ExtendedLivingRenderState) state).getEntity();
        PlayerData data = (PlayerData) player;
        //#else
        //$$public void setupAnimEnd(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
        //$$        float netHeadYaw, float headPitch, CallbackInfo info) {
        //$$    if (!(livingEntity instanceof PlayerData)) return;
        //$$    PlayerData data = (PlayerData) livingEntity;
        //$$    AbstractClientPlayer player = (AbstractClientPlayer) livingEntity;
        //#endif
        if (data.getPoseOverwrite() != null) {
            player.setPose(data.getPoseOverwrite());
            data.setPoseOverwrite(null);
        }
    }

}
