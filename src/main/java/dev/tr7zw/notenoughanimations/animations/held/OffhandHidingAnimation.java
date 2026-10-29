package dev.tr7zw.notenoughanimations.animations.held;

import dev.tr7zw.notenoughanimations.api.*;
import dev.tr7zw.notenoughanimations.util.*;
import dev.tr7zw.notenoughanimations.versionless.*;
import net.minecraft.client.model.*;
import net.minecraft.client.player.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;

import java.util.*;

public class OffhandHidingAnimation implements HeldAnimation {
    private Set<Item> hideItemsForTheseBows = new HashSet<>();

    @Override
    public void onLoad() {
        hideItemsForTheseBows.clear();
        hideItemsForTheseBows.addAll(AnimationUtil.parseItemList(NEABaseMod.config.hideItemsForTheseBows));
    }

    @Override
    public boolean applyAnimation(HeldAnimationData data) {
        if (NEABaseMod.config.enableOffhandHiding && data.getEntity() instanceof AbstractClientPlayer player
                && !(player.getMainHandItem().getItem() instanceof ShieldItem)
                && !AnimationUtil.hasUsePriority(player.getMainHandItem())) { // Don't hide still usable weapons
            boolean mainHandProjectileWeapon = player.getMainHandItem().getItem() instanceof ProjectileWeaponItem;
            boolean offHandProjectileWeapon = player.getOffhandItem().getItem() instanceof ProjectileWeaponItem;
            if (!mainHandProjectileWeapon) {
                mainHandProjectileWeapon = hideItemsForTheseBows.contains(player.getMainHandItem().getItem());
            }
            if (!offHandProjectileWeapon) {
                offHandProjectileWeapon = hideItemsForTheseBows.contains(player.getOffhandItem().getItem());
            }
            boolean projectileWeaponEquipped = mainHandProjectileWeapon || offHandProjectileWeapon;
            boolean mainHandCharged = AnimationUtil.isChargedCrossbow(player.getMainHandItem());
            boolean offHandCharged = AnimationUtil.isChargedCrossbow(player.getOffhandItem());
            boolean isUsingItem = player.isUsingItem();
            if (!mainHandCharged && isUsingItem) {
                //? if >= 1.21.0 {

                mainHandCharged = ((float) (player.getMainHandItem().getUseDuration(player)
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getMainHandItem(), player) >= 1.0f);
                //? } else {
                /*
                 mainHandCharged = ((float) (player.getMainHandItem().getUseDuration()
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getMainHandItem()) >= 1.0f);
                *///? }
            }
            if (!offHandCharged && isUsingItem) {
                //? if >= 1.21.0 {

                offHandCharged = ((float) (player.getOffhandItem().getUseDuration(player)
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getOffhandItem(), player) >= 1.0f);
                //? } else {
                /*
                 offHandCharged = ((float) (player.getOffhandItem().getUseDuration()
                        - player.getUseItemRemainingTicks())
                        / (float) CrossbowItem.getChargeDuration(player.getOffhandItem()) >= 1.0f);
                *///? }
            }

            HumanoidModel.ArmPose mainHandPose = AnimationUtil.getArmPose(player, InteractionHand.MAIN_HAND);
            HumanoidModel.ArmPose offHandPose = AnimationUtil.getArmPose(player, InteractionHand.OFF_HAND);
            if (!(AnimationUtil.isUsingBothHands(mainHandPose) || AnimationUtil.isUsingBothHands(offHandPose)
                    || (projectileWeaponEquipped && (mainHandCharged || offHandCharged || isUsingItem)))) {
                return false;
            }

            if (mainHandPose.isTwoHanded()) {
                offHandPose = player.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY
                        : HumanoidModel.ArmPose.ITEM;
            }

            HumanoidArm mainArm = HumanoidArm.RIGHT;
            HumanoidArm offArm = HumanoidArm.LEFT;
            if (player.getMainArm() == HumanoidArm.LEFT) {
                mainArm = HumanoidArm.LEFT;
                offArm = HumanoidArm.RIGHT;
            }
            var arm = data.getArm();
            if (arm == mainArm && AnimationUtil.isUsingBothHands(offHandPose)) {
                return true;
            } else if (arm == offArm && AnimationUtil.isUsingBothHands(mainHandPose)) {
                return true;
            } else if (projectileWeaponEquipped && (mainHandCharged || offHandCharged || isUsingItem)
                    && !((mainHandCharged || offHandCharged) && isUsingItem) // yay, 1.21.1 is weird!! ~ EW
                    && !(AnimationUtil.isUsingBothHands(mainHandPose) || AnimationUtil.isUsingBothHands(offHandPose))) {
                // Some mods like Archer have non-vanilla friendly animation systems that don't trigger
                // our both hands check. So, we compromise. If all else fails, we do this hacky mess! - EW
                if (arm == mainArm && offHandProjectileWeapon && !mainHandProjectileWeapon) {
                    return true;
                } else if (arm == offArm && mainHandProjectileWeapon) {
                    return true;
                }
            }
        }
        return false;
    }
}
