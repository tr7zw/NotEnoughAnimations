package dev.tr7zw.notenoughanimations.util;

import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import dev.tr7zw.util.NMSHelper;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class AnimationUtil {

    private static Item crossbow = NMSHelper.getItem(NMSHelper.getResourceLocation("minecraft", "crossbow"));

    public static boolean isUsingboothHands(ArmPose pose) {
        return pose == ArmPose.BOW_AND_ARROW || pose == ArmPose.CROSSBOW_CHARGE || pose == ArmPose.CROSSBOW_HOLD;
    }

    public static boolean isSwingingArm(AbstractClientPlayer player, BodyPart arm) {
        if (!player.swinging) {
            return false;
        }
        if (arm == BodyPart.LEFT_ARM) {
            return (player.getMainArm() == HumanoidArm.LEFT && player.swingingArm == InteractionHand.MAIN_HAND)
                    || (player.getMainArm() == HumanoidArm.RIGHT && player.swingingArm == InteractionHand.OFF_HAND);
        } else {
            return (player.getMainArm() == HumanoidArm.RIGHT && player.swingingArm == InteractionHand.MAIN_HAND)
                    || (player.getMainArm() == HumanoidArm.LEFT && player.swingingArm == InteractionHand.OFF_HAND);
        }
    }

    public static ArmPose getArmPose(AbstractClientPlayer abstractClientPlayerEntity, InteractionHand hand) {
        ItemStack itemStack = abstractClientPlayerEntity.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            return ArmPose.EMPTY;
        } else {
            if (abstractClientPlayerEntity.getUsedItemHand() == hand
                    && abstractClientPlayerEntity.getUseItemRemainingTicks() > 0) {
                UseAnim useAction = itemStack.getUseAnimation();
                if (useAction == UseAnim.BLOCK) {
                    return ArmPose.BLOCK;
                }

                if (useAction == UseAnim.BOW) {
                    return ArmPose.BOW_AND_ARROW;
                }

                if (useAction == UseAnim.SPEAR) {
                    return ArmPose.THROW_SPEAR;
                }

                // spotless:off
              //#if MC >= 11700
                if (useAction == UseAnim.SPYGLASS) {
                    return ArmPose.SPYGLASS;
                }
              //#endif
              //spotless:on

                if (useAction == UseAnim.CROSSBOW && hand.equals(abstractClientPlayerEntity.getUsedItemHand())) {
                    return ArmPose.CROSSBOW_CHARGE;
                }
            } else if (!abstractClientPlayerEntity.swinging && itemStack.getItem().equals(crossbow)
                    && isChargedCrossbow(itemStack)) {
                return ArmPose.CROSSBOW_HOLD;
            }

            return ArmPose.ITEM;
        }
    }

    public static boolean isChargedCrossbow(ItemStack item) {
        return CrossbowItem.isCharged(item);
    }

    public static void applyArmTransforms(PlayerModel<AbstractClientPlayer> model, HumanoidArm arm, float pitch,
            float yaw, float roll) {
        ModelPart part = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        part.xRot = pitch;
        part.yRot = yaw;
        if (arm == HumanoidArm.LEFT) // Just mirror yaw for the left hand
            part.yRot *= -1;
        part.zRot = roll;
        if (arm == HumanoidArm.LEFT)
            part.zRot *= -1;
    }

    public static void applyTransforms(PlayerModel<AbstractClientPlayer> model, BodyPart bodyPart, float pitch,
            float yaw, float roll) {
        ModelPart part;
        boolean mirror = false;
        switch (bodyPart) {
        case LEFT_ARM:
            mirror = true;
            part = model.leftArm;
            break;
        case RIGHT_ARM:
            part = model.rightArm;
            break;
        case LEFT_LEG:
            mirror = true;
            part = model.leftLeg;
            break;
        case RIGHT_LEG:
            part = model.rightLeg;
            break;
        default:
            return;
        }
        part.xRot = pitch;
        part.yRot = yaw;
        if (mirror) // Just mirror yaw for the left body parts
            part.yRot *= -1;
        part.zRot = roll;
        if (mirror)
            part.zRot *= -1;
    }

    public static void minMaxHeadRotation(Player livingEntity, PlayerModel<AbstractClientPlayer> model) {
        float value = legacyWrapDegrees(model.head.yRot);
        float min = legacyWrapDegrees(model.body.yRot - NMSHelper.HALF_PI);
        float max = legacyWrapDegrees(model.body.yRot + NMSHelper.HALF_PI);
        value = Math.min(value, max);
        value = Math.max(value, min);
        model.head.yRot = value;
        model.hat.yRot = value;
    }

    public static float interpolateRotation(float start, float end, float amount) {
        float wrappedStart = wrapDegrees(start);
        float wrappedEnd = wrapDegrees(end);

        float diff = wrappedEnd - wrappedStart;

        if (diff > NMSHelper.PI) {
            wrappedEnd -= NMSHelper.TWO_PI; // Ensure shortest path when difference is greater than PI
        } else if (diff < -NMSHelper.PI) {
            wrappedEnd += NMSHelper.TWO_PI; // Ensure shortest path when difference is less than -PI
        }

        return wrapDegrees(wrappedStart + (wrappedEnd - wrappedStart) * amount);
    }

    public static float lerpAngle(float delta, float start, float end) {
        float wrappedStart = wrapDegrees(start);
        float wrappedEnd = wrapDegrees(end);

        float difference = wrappedEnd - wrappedStart;
        float shortestPath = ((difference + NMSHelper.PI) % NMSHelper.TWO_PI) - NMSHelper.PI;

        return wrapDegrees(wrappedStart + shortestPath * delta);
    }

    public static float wrapDegrees(float angle) {
        return ((angle + NMSHelper.PI) % NMSHelper.TWO_PI) - NMSHelper.PI;
    }

    public static float legacyWrapDegrees(float f) {
        float g = f % 6.28318512f;
        if (g >= 3.14159256f) {
            g -= 6.28318512f;
        }
        if (g < -3.14159256f) {
            g += 6.28318512f;
        }
        return g;
    }

}
