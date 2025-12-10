package dev.tr7zw.notenoughanimations.mixins;

//? if >= 1.21.2 {

import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
//? }
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
//? if >= 1.21.9 {

public abstract class PlayerEntityModelMixin
        extends HumanoidModel<net.minecraft.client.renderer.entity.state.AvatarRenderState> {
    //? } else if >= 1.21.2 {
/*
     public abstract class PlayerEntityModelMixin extends HumanoidModel<net.minecraft.client.renderer.entity.state.PlayerRenderState> {
    *///? } else {
    /*
     public abstract class PlayerEntityModelMixin<T extends net.minecraft.world.entity.LivingEntity> extends HumanoidModel<T> {
    *///? }

    @Unique
    //? if >= 1.21.2 && < 1.21.9 {
/*
     private static final String SETUP_ANIM_METHOD = "setupAnim(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;)V";
    *///? } else {

    private static final String SETUP_ANIM_METHOD = "setupAnim";
    //? }

    public PlayerEntityModelMixin() {
        //? if >= 1.17.0 {

        super(null);
        //? } else {

        // super(0);
        //? }

    }

    @Inject(method = SETUP_ANIM_METHOD, at = @At(value = "HEAD"))
    //? if >= 1.21.2 {

    public void setupAnimHEAD(
            //? if >= 1.21.9 {

            net.minecraft.client.renderer.entity.state.AvatarRenderState state,
            //? } else {
            /*
                     net.minecraft.client.renderer.entity.state.PlayerRenderState  state,
                    *///? }
            CallbackInfo info) {
        if (state == null || !(state instanceof ExtendedLivingRenderState)) {
            return;
        }
        float limbSwing = state.walkAnimationPos; // makes total sense :thumbs_up:
        PlayerModel model = (PlayerModel) (Object) this;
        AbstractClientPlayer player = null;
        if (((ExtendedLivingRenderState) state).getEntity() != null
                && ((ExtendedLivingRenderState) state).getEntity() instanceof AbstractClientPlayer p) {
            player = p;
        }
        if (player == null) {
            return;
        }
        //? } else {
        /*
         public void setupAnimHEAD(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
                float netHeadYaw, float headPitch, CallbackInfo info) {
            PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) (Object) this;
            if (!(livingEntity instanceof AbstractClientPlayer)) return;
            AbstractClientPlayer player = (AbstractClientPlayer) livingEntity;
        *///? }
        NEAnimationsLoader.INSTANCE.playerTransformer.preUpdate(player, model, limbSwing, info);
    }

    //? if >= 1.21.2 {

    @Inject(method = SETUP_ANIM_METHOD, at = @At(value = "RETURN"))
    public void setupAnim(
            //? if >= 1.21.9 {

            net.minecraft.client.renderer.entity.state.AvatarRenderState state,
            //? } else {
            /*
                     net.minecraft.client.renderer.entity.state.PlayerRenderState  state,
                    *///? }
            CallbackInfo info) {
        float limbSwing = state.walkAnimationPos; // makes total sense :thumbs_up:
        PlayerModel model = (PlayerModel) (Object) this;
        AbstractClientPlayer player = null;
        if (((ExtendedLivingRenderState) state).getEntity() != null
                && ((ExtendedLivingRenderState) state).getEntity() instanceof AbstractClientPlayer p) {
            player = p;
        }
        if (player == null) {
            return;
        }
        //? } else {
        /*
         @Inject(method = "setupAnim", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V", ordinal = 0))
         public void setupAnim(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
                float netHeadYaw, float headPitch, CallbackInfo info) {
            PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) (Object) this;
            if (!(livingEntity instanceof AbstractClientPlayer)) return;
            AbstractClientPlayer player = (AbstractClientPlayer) livingEntity;
        *///? }
        NEAnimationsLoader.INSTANCE.playerTransformer.updateModel(player, model, limbSwing, info);
    }

    @Inject(method = SETUP_ANIM_METHOD, at = @At(value = "RETURN"))
    //? if >= 1.21.2 {

    public void setupAnimEnd(
            //? if >= 1.21.9 {

            net.minecraft.client.renderer.entity.state.AvatarRenderState state,
            //? } else {
            /*
                     net.minecraft.client.renderer.entity.state.PlayerRenderState state,
                    *///? }
            CallbackInfo info) {
        AbstractClientPlayer player = null;
        if (((ExtendedLivingRenderState) state).getEntity() != null
                && ((ExtendedLivingRenderState) state).getEntity() instanceof AbstractClientPlayer p) {
            player = p;
        }
        if (player == null) {
            return;
        }
        PlayerData data = (PlayerData) player;
        //? } else {
        /*
         public void setupAnimEnd(T livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks,
                float netHeadYaw, float headPitch, CallbackInfo info) {
            if (!(livingEntity instanceof PlayerData)) return;
            PlayerData data = (PlayerData) livingEntity;
            AbstractClientPlayer player = (AbstractClientPlayer) livingEntity;
        *///? }
        if (data.getPoseOverwrite() != null) {
            player.setPose(data.getPoseOverwrite());
            data.setPoseOverwrite(null);
        }
    }

}
