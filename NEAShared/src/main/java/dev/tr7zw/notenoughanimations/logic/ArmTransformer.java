package dev.tr7zw.notenoughanimations.logic;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.util.VanillaAnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ArmTransformer {

    private Set<Item> compatibleMaps = new HashSet<>();
    private Set<Item> holdingItems = new HashSet<>();

    private boolean doneLatebind = false;
    private final Minecraft mc = Minecraft.getInstance();
    private int frameId = 0; // ok to overflow, just used to keep track of what has been updated this frame
    private boolean renderingFirstPersonArm = false;

    public void updateArms(AbstractClientPlayer entity, PlayerModel<AbstractClientPlayer> model, float tick, float f,
            CallbackInfo info) {
        if (!doneLatebind)
            lateBind();
        if (mc.level == null) { // We are in a main menu or something
            return;
        }
        if (entity instanceof AbstractClientPlayer && entity.getPose() == Pose.SWIMMING) { // Crawling/Swimming has its own animations
                                                                             // and messing with it screws stuff up
            if (!entity.isInWater() && NEAnimationsLoader.config.enableCrawlingAnimation)
                fixSwimmingOutOfWater(entity, model, f);
            return;
        }
        boolean rightHanded = entity.getMainArm() == HumanoidArm.RIGHT;
        applyAnimations(entity, model, HumanoidArm.RIGHT,
                rightHanded ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, tick);
        applyAnimations(entity, model, HumanoidArm.LEFT,
                !rightHanded ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, tick);

        if (NEAnimationsLoader.config.enableAnimationSmoothing && entity instanceof PlayerData) {
            PlayerData data = (PlayerData) entity;
            if (renderingFirstPersonArm) {
                return; // this is rendering the first person hand, don't try to use that for
                        // interpolation
            }
            float[] last = data.getLastRotations();
            boolean differentFrame = !data.isUpdated(frameId);
            long timePassed = System.currentTimeMillis() - data.lastUpdate();
            if (timePassed < 1)
                timePassed = 1;
            interpolate(model.leftArm, last, 0, timePassed, differentFrame,
                    NEAnimationsLoader.config.animationSmoothingSpeed);
            interpolate(model.rightArm, last, 3, timePassed, differentFrame,
                    NEAnimationsLoader.config.animationSmoothingSpeed);
            data.setUpdated(frameId);
        }
    }

    private void fixSwimmingOutOfWater(AbstractClientPlayer entity, PlayerModel<AbstractClientPlayer> model, float f) {
        float swimAmount = model.swimAmount;
        float attackTime = model.attackTime;
        float speedMul = 2.5F;
        if (swimAmount > 0.0F) {
            float l = f * speedMul % 26.0F;
            HumanoidArm humanoidArm = getAttackArm(entity);
            float m = (humanoidArm == HumanoidArm.RIGHT && attackTime > 0.0F) ? 0.0F : swimAmount;
            float n = (humanoidArm == HumanoidArm.LEFT && attackTime > 0.0F) ? 0.0F : swimAmount;
            float armMoveHight = 0.3707964F;
            ArmPose rightArmPose = VanillaAnimationUtil.getArmPose(entity, entity.getMainArm() == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            ArmPose leftArmPose = VanillaAnimationUtil.getArmPose(entity, entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            if ((rightArmPose == ArmPose.BLOCK || rightArmPose == ArmPose.ITEM || rightArmPose == ArmPose.EMPTY) && leftArmPose != ArmPose.CROSSBOW_CHARGE && leftArmPose != ArmPose.BOW_AND_ARROW)
                if (l < 14.0F) {
                    model.rightArm.xRot = Mth.lerp(m, model.rightArm.xRot, 0.0F);
                    model.rightArm.yRot = Mth.lerp(m, model.rightArm.yRot, 3.1415927F);
                    model.rightArm.zRot = Mth.lerp(m, model.rightArm.zRot,
                            3.1415927F - 1.8707964F * quadraticArmUpdate(l) / quadraticArmUpdate(14.0F));
                } else if (l >= 14.0F && l < 24.0F) {
                    float o = (l - 14.0F) / 10.0F;
                    model.rightArm.xRot = Mth.lerp(m, model.rightArm.xRot, -armMoveHight * o);
                    model.rightArm.yRot = Mth.lerp(m, model.rightArm.yRot, 3.1415927F);
                    model.rightArm.zRot = Mth.lerp(m, model.rightArm.zRot, 1.2707963F + 1.8707964F * o);
                } else if (l >= 24.0F && l < 26.0F) {
                    float p = (l - 24.0F) / 2.0F;
                    model.rightArm.xRot = Mth.lerp(m, model.rightArm.xRot, -armMoveHight + armMoveHight * p);
                    model.rightArm.yRot = Mth.lerp(m, model.rightArm.yRot, 3.1415927F);
                    model.rightArm.zRot = Mth.lerp(m, model.rightArm.zRot, 3.1415927F);
                }
            l += 13F;
            l %= 26F;
            if ((leftArmPose == ArmPose.BLOCK || leftArmPose == ArmPose.ITEM || leftArmPose == ArmPose.EMPTY) && rightArmPose != ArmPose.CROSSBOW_CHARGE && rightArmPose != ArmPose.BOW_AND_ARROW)
                if (l < 14.0F) {
                    model.leftArm.xRot = rotlerpRad(n, model.leftArm.xRot, 0.0F);
                    model.leftArm.yRot = rotlerpRad(n, model.leftArm.yRot, 3.1415927F);
                    model.leftArm.zRot = rotlerpRad(n, model.leftArm.zRot,
                            3.1415927F + 1.8707964F * quadraticArmUpdate(l) / quadraticArmUpdate(14.0F));
                } else if (l >= 14.0F && l < 24.0F) {
                    float o = (l - 14.0F) / 10.0F;
                    model.leftArm.xRot = rotlerpRad(n, model.leftArm.xRot, -armMoveHight * o);
                    model.leftArm.yRot = rotlerpRad(n, model.leftArm.yRot, 3.1415927F);
                    model.leftArm.zRot = rotlerpRad(n, model.leftArm.zRot, 5.012389F - 1.8707964F * o);
                } else if (l >= 24.0F && l < 26.0F) {
                    float p = (l - 24.0F) / 2.0F;
                    model.leftArm.xRot = rotlerpRad(n, model.leftArm.xRot, -armMoveHight + armMoveHight * p);
                    model.leftArm.yRot = rotlerpRad(n, model.leftArm.yRot, 3.1415927F);
                    model.leftArm.zRot = rotlerpRad(n, model.leftArm.zRot, 3.1415927F);
                }
        }
        float q = 0.15F;
        float r = 0.33333334F;
        f *= speedMul;
        model.leftLeg.xRot = Mth.lerp(swimAmount, model.leftLeg.xRot, q * Mth.cos(f * r + 3.1415927F));
        model.rightLeg.xRot = Mth.lerp(swimAmount, model.rightLeg.xRot, q * Mth.cos(f * r));
        model.rightLeg.zRot = 0.1507964F;
        model.leftLeg.zRot = -0.1507964F;
    }

    private float rotlerpRad(float f, float g, float h) {
        float i = (h - g) % 6.2831855F;
        if (i < -3.1415927F)
            i += 6.2831855F;
        if (i >= 3.1415927F)
            i -= 6.2831855F;
        return g + f * i;
    }

    private float quadraticArmUpdate(float f) {
        return -65.0F * f + f * f;
    }

    private HumanoidArm getAttackArm(Player livingEntity) {
        HumanoidArm humanoidArm = livingEntity.getMainArm();
        return (livingEntity.swingingArm == InteractionHand.MAIN_HAND) ? humanoidArm : humanoidArm.getOpposite();
    }

    public void nextFrame() {
        frameId++;
    }

    public void renderingFirstPersonArm(boolean flag) {
        this.renderingFirstPersonArm = flag;
    }

    private void interpolate(ModelPart model, float[] last, int offset, long timePassed, boolean differentFrame,
            float speed) {
        if (!differentFrame) { // Rerendering the place in the same frame
            model.xRot = (last[offset]);
            model.yRot = (last[offset + 1]);
            model.zRot = (last[offset + 2]);
            return;
        }
        if (timePassed > 200) { // Don't try to interpolate states older than 200ms
            last[offset] = model.xRot;
            last[offset + 1] = model.yRot;
            last[offset + 2] = model.zRot;
            cleanInvalidData(last, offset);
            return;
        }
        float amount = ((1f / (1000f / timePassed))) * speed;
        if (amount > 1)
            amount = 1;
        last[offset] = last[offset] + ((model.xRot - last[offset]) * amount);
        last[offset + 1] = last[offset + 1] + ((wrapDegrees(model.yRot) - wrapDegrees(last[offset + 1])) * amount);
        last[offset + 2] = last[offset + 2] + ((model.zRot - last[offset + 2]) * amount);
        cleanInvalidData(last, offset);
        model.xRot = (last[offset]);
        model.yRot = (last[offset + 1]);
        model.zRot = (last[offset + 2]);
    }

    /**
     * When using a quickcharge 5 crossbow it is able to cause NaN values to show up
     * because of how broken it is.
     * 
     * @param data
     * @param offset
     */
    private void cleanInvalidData(float[] data, int offset) {
        if (Float.isNaN(data[offset]))
            data[offset] = 0;
        if (Float.isNaN(data[offset + 1]))
            data[offset + 1] = 0;
        if (Float.isNaN(data[offset + 2]))
            data[offset + 2] = 0;
    }

    private void applyAnimations(AbstractClientPlayer livingEntity, PlayerModel<AbstractClientPlayer> model, HumanoidArm arm,
            InteractionHand hand, float tick) {
        ItemStack itemInHand = livingEntity.getItemInHand(hand);
        ItemStack itemInOtherHand = livingEntity.getItemInHand(
                hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        // passive animations
        ArmPose pose = VanillaAnimationUtil.getArmPose(livingEntity, hand);
        ArmPose poseOtherHand = VanillaAnimationUtil.getArmPose(livingEntity, hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if((pose == ArmPose.BLOCK || pose == ArmPose.ITEM) && !poseOtherHand.isTwoHanded()) // seperated for logic, better than building a multi line if filled with ()&&||
            if ((NEAnimationsLoader.config.holdUpAllItems && !itemInHand.isEmpty() && !livingEntity.swinging) || holdingItems.contains(itemInHand.getItem())) {
                applyArmTransforms(model, arm, -(Mth.lerp(-1f * (livingEntity.getXRot() - 90f) / 180f, 1f, 1.5f)), -0.2f,
                        0.3f);
            }
        if (NEAnimationsLoader.config.enableInWorldMapRendering) {
            if ((compatibleMaps.contains(itemInHand.getItem()) && itemInOtherHand.isEmpty()
                    && hand == InteractionHand.MAIN_HAND)
                    || compatibleMaps.contains(itemInOtherHand.getItem()) && itemInHand.isEmpty()
                            && hand == InteractionHand.OFF_HAND) {
                applyArmTransforms(model, arm, -(Mth.lerp(-1f * (livingEntity.getXRot() - 90f) / 180f, 0.5f, 1.5f)),
                        -0.4f, 0.3f);
            } else if (compatibleMaps.contains(itemInHand.getItem()) && hand == InteractionHand.MAIN_HAND) {
                applyArmTransforms(model, arm, -(Mth.lerp(-1f * (livingEntity.getXRot() - 90f) / 180f, 0.5f, 1.5f)), 0f,
                        0.3f);
            }
            if (compatibleMaps.contains(itemInHand.getItem()) && hand == InteractionHand.OFF_HAND) {
                applyArmTransforms(model, arm, -(Mth.lerp(-1f * (livingEntity.getXRot() - 90f) / 180f, 0.5f, 1.5f)), 0f,
                        0.3f);
            }
        }
        if (livingEntity.isSleeping()) {
            if (NEAnimationsLoader.config.freezeArmsInBed) {
                applyArmTransforms(model, arm, 0, 0f, 0f);
            }
            return; // Dont try to apply more
        }
        // Stop here if the hands are doing something

        if (livingEntity.getUsedItemHand() == hand && livingEntity.getUseItemRemainingTicks() > 0) {
            UseAnim action = itemInHand.getUseAnimation();
            if (action == UseAnim.BLOCK || action == UseAnim.SPEAR || action == UseAnim.BOW
                    || action == UseAnim.CROSSBOW) {
                return;// stop
            }
        }
        if (renderingFirstPersonArm) {
            return; // Don't apply 3rd person animations to the first person arm
        }
        // active animations
        if (livingEntity.isPassenger()) {
            if (livingEntity.getVehicle() instanceof Boat && NEAnimationsLoader.config.enableRowBoatAnimation) {
                Boat boat = (Boat) livingEntity.getVehicle();
                float paddle = boat.getRowingTime(arm == HumanoidArm.LEFT ? 0 : 1, tick);
                applyArmTransforms(model, arm, -1.1f - Mth.sin(paddle) * 0.3f, 0.2f, 0.3f);
            }
            if (livingEntity.getVehicle() instanceof AbstractHorse && NEAnimationsLoader.config.enableHorseAnimation) {
                AbstractHorse horse = (AbstractHorse) livingEntity.getVehicle();
                float rotation = -Mth.cos(horse.animationPosition * 0.3f);
                rotation *= 0.1;
                applyArmTransforms(model, arm, -1.1f - rotation, -0.2f, 0.3f);
            }
        }
        if (livingEntity.onClimbable() && NEAnimationsLoader.config.enableLadderAnimation && !livingEntity.isOnGround()) {
            if(NEAnimationsLoader.config.enableRotateToLadder) {
                BlockState blockState = livingEntity.getFeetBlockState();
                if (blockState.hasProperty(HorizontalDirectionalBlock.FACING)) {
                    Direction dir = blockState.getValue(HorizontalDirectionalBlock.FACING);
                    if(livingEntity instanceof PlayerData data) {
                        data.disableBodyRotation(true);
                    }
                    switch (dir) {
                    case NORTH:
                        livingEntity.setYBodyRot(0);
                        livingEntity.yBodyRotO = 0;
                        break;
                    case EAST:
                        livingEntity.setYBodyRot(90);
                        livingEntity.yBodyRotO = 90;
                        break;
                    case SOUTH:
                        livingEntity.setYBodyRot(180);
                        livingEntity.yBodyRotO = 180;
                        break;
                    case WEST:
                        livingEntity.setYBodyRot(270);
                        livingEntity.yBodyRotO = 270;
                        break;
                    default:
                    }
                    minMaxHeadRotation(livingEntity, model);
                }
            }
            float rotation = -Mth.cos((float) (livingEntity.getY() * NEAnimationsLoader.config.ladderAnimationArmSpeed));
            rotation *= NEAnimationsLoader.config.ladderAnimationAmplifier;
            if (arm == HumanoidArm.LEFT)
                rotation *= -1;
            applyArmTransforms(model, arm, -NEAnimationsLoader.config.ladderAnimationArmHeight - rotation, -0.2f, 0.3f);
        }
        if (livingEntity.getUsedItemHand() == hand && livingEntity.getUseItemRemainingTicks() > 0
                && NEAnimationsLoader.config.enableEatDrinkAnimation) {
            UseAnim action = itemInHand.getUseAnimation();
            // Eating/Drinking
            if (action == UseAnim.EAT || action == UseAnim.DRINK) {
                applyArmTransforms(model, arm,
                        -(Mth.lerp(-1f * (livingEntity.getXRot() - 90f) / 180f, 1f, 2f)) + Mth.sin((System.currentTimeMillis() % 20000) / 30f) * 0.1f,
                        -0.3f, 0.3f);
            }
        }
    }
    
    private void minMaxHeadRotation(Player livingEntity, PlayerModel<AbstractClientPlayer> model) {
        float value = wrapDegrees(model.head.yRot);
        float min = wrapDegrees(model.body.yRot - Mth.HALF_PI);
        float max = wrapDegrees(model.body.yRot + Mth.HALF_PI);
        value = Math.min(value, max);
        value = Math.max(value, min);
        model.head.yRot = value;
        model.hat.yRot = value;
    }

    private void lateBind() {
        Item invalid = Registry.ITEM.get(new ResourceLocation("minecraft", "air"));
        for (String itemId : NEAnimationsLoader.config.holdingItems) {
            try {
                Item item = Registry.ITEM.get(new ResourceLocation(itemId.split(":")[0], itemId.split(":")[1]));
                if (invalid != item)
                    holdingItems.add(item);
            } catch (Exception ex) {
                System.err.println("Unknown item to add to the holding list: " + itemId);
            }
        }
        compatibleMaps.add(Registry.ITEM.get(new ResourceLocation("minecraft", "filled_map")));
        Item antiqueAtlas = Registry.ITEM.get(new ResourceLocation("antiqueatlas", "antique_atlas"));
        if (invalid != antiqueAtlas) {
            compatibleMaps.add(antiqueAtlas);
            NEAnimationsLoader.LOGGER.info("Added AntiqueAtlas support to Not Enough Animations!");
        }
        doneLatebind = true;
    }

    private void applyArmTransforms(PlayerModel<AbstractClientPlayer> model, HumanoidArm arm, float pitch, float yaw,
            float roll) {
        ModelPart part = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        part.xRot = pitch;
        part.yRot = yaw;
        if (arm == HumanoidArm.LEFT) // Just mirror yaw for the left hand
            part.yRot *= -1;
        part.zRot = roll;
        if (arm == HumanoidArm.LEFT)
            part.zRot *= -1;
    }

    private float wrapDegrees(float f) {
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
