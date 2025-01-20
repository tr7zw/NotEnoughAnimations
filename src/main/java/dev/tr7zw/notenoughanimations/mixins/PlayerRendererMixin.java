package dev.tr7zw.notenoughanimations.mixins;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.util.RenderStateHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.renderlayer.SwordRenderLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//#if MC >= 12102
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
//#endif
//#if MC >= 11700
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
//#else
//$$ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//#endif

@Mixin(PlayerRenderer.class)
//#if MC >= 12102
public abstract class PlayerRendererMixin
        extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {
    //#else
    //$$public abstract class PlayerRendererMixin
    //$$        extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    //#endif

    //#if MC >= 12102
    public PlayerRendererMixin(Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }
    //#elseif MC >= 11700
    //$$public PlayerRendererMixin(Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
    //$$    super(context, entityModel, f);
    //$$}
    //#else
    //$$     public PlayerRendererMixin(EntityRenderDispatcher entityRenderDispatcher,
    //$$    PlayerModel<AbstractClientPlayer> entityModel, float f) {
    //$$    super(entityRenderDispatcher, entityModel, f);
    //$$    }
    //#endif

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onCreate(CallbackInfo info) {
        this.addLayer(new SwordRenderLayer(this));
    }

    //#if MC >= 12102
    @Inject(method = "extractRenderState(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;F)V", at = @At("HEAD"))
    private void includeData(AbstractClientPlayer abstractClientPlayer, PlayerRenderState playerRenderState, float f,
            CallbackInfo ci) {
        if (abstractClientPlayer instanceof PlayerData playerData) {
            RenderStateHolder.RenderStateData data = playerData.getData(RenderStateHolder.INSTANCE,
                    RenderStateHolder.RenderStateData::new);
            data.renderState = playerRenderState;
        }
    }
    //#endif

}
