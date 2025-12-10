package dev.tr7zw.notenoughanimations.renderlayer;

import java.util.HashSet;
import java.util.Set;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.notenoughanimations.access.ExtendedLivingRenderState;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.transition.mc.GeneralUtil;
import dev.tr7zw.transition.mc.ItemUtil;
import dev.tr7zw.transition.mc.MathUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
//? if < 1.21.9 {
/*
 import net.minecraft.client.renderer.MultiBufferSource;
 import net.minecraft.client.model.PlayerModel;
 import net.minecraft.client.Minecraft;
 //? if >= 1.19.4 {

  import net.minecraft.world.item.ItemDisplayContext;
 //? } else {
/^
  import net.minecraft.client.renderer.block.model.ItemTransforms;
 ^///? }
*///? }
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//? if >= 1.21.2 {

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class SwordRenderLayer extends RenderLayer<HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
    //? } else {
    /*
     public class SwordRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    *///? }

    //? if >= 1.21.2 {

    public SwordRenderLayer(RenderLayerParent<HumanoidRenderState, HumanoidModel<HumanoidRenderState>> renderer) {
        super(renderer);
    }
    //? } else {
    /*
     public SwordRenderLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
     }
    *///? }

    private boolean lazyInit = true;
    private static Set<Item> items = new HashSet<>();
    private boolean disabled = false;

    public static void update(Player player) {
        PlayerData data = (PlayerData) player;
        if (items.contains(player.getMainHandItem().getItem())) {
            data.setSideSword(player.getMainHandItem());
        }
        if (items.contains(player.getOffhandItem().getItem())) {
            data.setSideSword(player.getOffhandItem());
        }
    }

    @Override
    //? if >= 1.21.9 {

    public void submit(PoseStack poseStack, net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector,
            int i, HumanoidRenderState entityRenderState, float f, float g) {
        AbstractClientPlayer player = (AbstractClientPlayer) ((ExtendedLivingRenderState) entityRenderState)
                .getEntity();
        //? } else if >= 1.21.2 {
        /*
         public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light,
                HumanoidRenderState entityRenderState, float f, float g) {
            AbstractClientPlayer player = (AbstractClientPlayer) ((ExtendedLivingRenderState) entityRenderState)
                    .getEntity();
        *///? } else if >= 1.19.4 {
        /*
         public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, AbstractClientPlayer player,
                float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5,
                float paramFloat6) {
        *///? } else {
        /*
             public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light,
             AbstractClientPlayer player, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
            float paramFloat5, float paramFloat6) {
        *///? }
        if (disabled || player == null) {
            return;
        }
        if (lazyInit) {
            lazyInit = false;
            init();
        }
        if (!NEABaseMod.config.showLastUsedSword) {
            return;
        }
        if (player.isInvisible() || player.isSleeping())
            return;
        if (!(player instanceof PlayerData)) {
            return;
        }
        if (player.isPassenger()) {
            return; // sitting player in a boat/minecart/on horse pokes the vehicle with the sword
        }
        PlayerData data = (PlayerData) player;
        ItemStack itemStack = data.getSideSword();
        if (itemStack.isEmpty())
            return;
        if (player.getMainHandItem() == itemStack || player.getOffhandItem() == itemStack) {
            return;
        }
        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        boolean lefthanded = (player.getMainArm() == HumanoidArm.LEFT);
        boolean wearingArmor = !player.getItemBySlot(EquipmentSlot.LEGS).isEmpty();
        if (!player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()
                && player.getItemBySlot(EquipmentSlot.CHEST).getItem() != Items.ELYTRA) {
            wearingArmor = true;
        }
        double offsetX = wearingArmor ? 0.3D : 0.28D;
        float swordRotation = -80F;
        if (lefthanded) {
            offsetX *= -1d;
        }
        poseStack.translate(offsetX, 0.85D, 0.25D);
        poseStack.mulPose(MathUtil.XP.rotationDegrees(swordRotation));
        poseStack.mulPose(MathUtil.YP.rotationDegrees(180.0F));

        //? if >= 1.21.9 {

        //? } else if >= 1.21.5 {
        /*
         Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(player, itemStack,
                lefthanded ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                poseStack, multiBufferSource, light);
        *///? } else if >= 1.19.4 {
        /*
         Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(player, itemStack,
                 lefthanded ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                lefthanded, poseStack, multiBufferSource, light);
        *///? } else if >= 1.19.0 {

        //     Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(player, itemStack, lefthanded ? ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND : ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, lefthanded,
        //     poseStack, multiBufferSource, light);
        //? } else {
        /*
         Minecraft.getInstance().getItemInHandRenderer().renderItem(player, itemStack, lefthanded ? ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND : ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, lefthanded,
          poseStack, multiBufferSource, light);
        *///? }
        poseStack.popPose();
    }

    private void init() {
        for (String itemKey : NEABaseMod.config.sheathSwords) {
            if (itemKey.contains(":")) {
                Item item = ItemUtil
                        .getItem(GeneralUtil.getResourceLocation(itemKey.split(":")[0], itemKey.split(":")[1]));
                if (item != Items.AIR) {
                    items.add(item);
                }
            }
        }
        try {
            // Disable when backslot is installed
            Class.forName("net.backslot.BackSlotMain");
            disabled = true;
        } catch (Throwable th) {

        }
    }
}
