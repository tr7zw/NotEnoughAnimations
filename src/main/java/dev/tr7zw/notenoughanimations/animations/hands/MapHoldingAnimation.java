package dev.tr7zw.notenoughanimations.animations.hands;

import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.util.NMSWrapper;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import dev.tr7zw.transition.mc.EntityUtil;
import dev.tr7zw.transition.mc.GeneralUtil;
import dev.tr7zw.transition.mc.ItemUtil;
//? if >= 1.21.11 {

import net.minecraft.client.model.player.*;
//? } else {
/*
import net.minecraft.client.model.*;
*///? }
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MapHoldingAnimation extends BasicAnimation {

    private Set<Item> compatibleMaps = new HashSet<>();

    @Override
    public boolean isEnabled() {
        bind();
        return NEABaseMod.config.enableInWorldMapRendering || !compatibleMaps.isEmpty();
    }

    private void bind() {
        compatibleMaps.clear();
        Item invalid = ItemUtil.getItem(GeneralUtil.getResourceLocation("minecraft", "air"));
        compatibleMaps.add(ItemUtil.getItem(GeneralUtil.getResourceLocation("minecraft", "filled_map")));
        Item antiqueAtlas = ItemUtil.getItem(GeneralUtil.getResourceLocation("antiqueatlas", "antique_atlas"));
        if (invalid != antiqueAtlas) {
            compatibleMaps.add(antiqueAtlas);
            NEABaseMod.LOGGER.info("Added AntiqueAtlas support to Not Enough Animations!");
        }
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        ItemStack itemInMainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack itemInOffHand = entity.getItemInHand(InteractionHand.OFF_HAND);
        if (compatibleMaps.contains(itemInMainHand.getItem()) && itemInOffHand.isEmpty()) {
            if (NMSWrapper.hasCustomModel(itemInMainHand)) {
                return false;
            } else {
                target = bothHands;
                return true;
            }
        }
        if (compatibleMaps.contains(itemInMainHand.getItem()) && !itemInOffHand.isEmpty()) {
            if (NMSWrapper.hasCustomModel(itemInMainHand)) {
                return false;
            } else {
                target = entity.getMainArm() == HumanoidArm.RIGHT ? right : left;
                return true;
            }

        }
        if (compatibleMaps.contains(itemInOffHand.getItem()) && !itemInOffHand.isEmpty()) {
            if (NMSWrapper.hasCustomModel(itemInOffHand)) {
                return false;
            } else {
                target = entity.getMainArm() == HumanoidArm.RIGHT ? left : right;
                return true;
            }
        }

        return false;
    }

    private final BodyPart[] bothHands = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM };
    private final BodyPart[] left = new BodyPart[] { BodyPart.LEFT_ARM };
    private final BodyPart[] right = new BodyPart[] { BodyPart.RIGHT_ARM };
    private BodyPart[] target = bothHands;

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return target;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 300;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        if (target == bothHands) {
            AnimationUtil.applyArmTransforms(model, arm,
                    -(Mth.lerp(-1f * (EntityUtil.getXRot(entity) - 90f) / 180f, 0.7f, 0.9f)),
                    (Mth.lerp(-1f * (EntityUtil.getXRot(entity) - 90f) / 180f, -0.3f, -0.2f)), 0.3f);
            return;
        }
        AnimationUtil.applyArmTransforms(model, arm,
                -(Mth.lerp(-1f * (EntityUtil.getXRot(entity) - 90f) / 180f, 0.5f, 1.5f)), 0f, 0.3f);
    }

}
