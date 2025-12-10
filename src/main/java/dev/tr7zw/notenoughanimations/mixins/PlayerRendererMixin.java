package dev.tr7zw.notenoughanimations.mixins;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.util.RenderStateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.renderlayer.SwordRenderLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
//? if >= 1.21.2 {

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
//? }
//? if >= 1.17.0 {

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
//? } else {

// import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//? }

//? if >= 1.21.9 {

@Mixin(net.minecraft.client.renderer.entity.player.AvatarRenderer.class)
//? } else {
/*
 @Mixin(net.minecraft.client.renderer.entity.player.PlayerRenderer.class)
*///? }
   //? if >= 1.21.2 {

public abstract class PlayerRendererMixin
        extends LivingEntityRenderer<AbstractClientPlayer, HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
    //? } else {
    /*
     public abstract class PlayerRendererMixin
            extends LivingEntityRenderer<AbstractClientPlayer, net.minecraft.client.model.PlayerModel<AbstractClientPlayer>> {
    *///? }

    //? if >= 1.21.9 {

    public PlayerRendererMixin(Context context, HumanoidModel<HumanoidRenderState> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }
    //? } else if >= 1.21.2 {
    /*
     public PlayerRendererMixin(Context context, net.minecraft.client.model.HumanoidModel model, float shadowRadius) {
        super(context, model, shadowRadius);
     }
    *///? } else if >= 1.17.0 {
    /*
     public PlayerRendererMixin(Context context, net.minecraft.client.model.PlayerModel<AbstractClientPlayer> entityModel, float f) {
        super(context, entityModel, f);
     }
    *///? } else {

    //     public PlayerRendererMixin(EntityRenderDispatcher entityRenderDispatcher,
    //    net.minecraft.client.model.PlayerModel<AbstractClientPlayer> entityModel, float f) {
    //    super(entityRenderDispatcher, entityModel, f);
    //    }
    //? }

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onCreate(CallbackInfo info) {
        //? if < 1.21.9 {
        /*
         this.addLayer(new SwordRenderLayer(this));
        *///? }
    }

    //? if >= 1.21.9 {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("HEAD"))
    private void includeData(net.minecraft.world.entity.Avatar abstractClientPlayer,
            net.minecraft.client.renderer.entity.state.AvatarRenderState playerRenderState, float f, CallbackInfo ci) {
        if (abstractClientPlayer instanceof PlayerData playerData) {
            RenderStateHolder.RenderStateData data = playerData.getData(RenderStateHolder.INSTANCE,
                    RenderStateHolder.RenderStateData::new);
            data.renderState = playerRenderState;
        }
    }
    //? } else if >= 1.21.2 {
    /*
     @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("HEAD"))
     private void includeData(AbstractClientPlayer abstractClientPlayer, net.minecraft.client.renderer.entity.state.PlayerRenderState playerRenderState, float f,
            CallbackInfo ci) {
        if (abstractClientPlayer instanceof PlayerData playerData) {
            RenderStateHolder.RenderStateData data = playerData.getData(RenderStateHolder.INSTANCE,
                    RenderStateHolder.RenderStateData::new);
            data.renderState = playerRenderState;
        }
     }
    *///? }

}
