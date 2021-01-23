/*package dev.tr7zw.notenoughanimations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> {

	public PlayerEntityModelMixin(float scale) {
		super(scale);
	}

	@Inject(method = "setAngles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;copyPositionAndRotation(Lnet/minecraft/client/model/ModelPart;)V", ordinal = 0))
	public void setAngles(T livingEntity, float f, float g, float tick, float i, float j, CallbackInfo info) {
		boolean rightHanded = livingEntity.getMainArm() == Arm.RIGHT;
		applyAnimations(livingEntity, Arm.RIGHT, rightHanded ? Hand.MAIN_HAND : Hand.OFF_HAND, tick);
		applyAnimations(livingEntity, Arm.LEFT, !rightHanded ? Hand.MAIN_HAND : Hand.OFF_HAND, tick);
	}

	private void applyAnimations(T livingEntity, Arm arm, Hand hand, float tick) {
		ItemStack itemInHand = livingEntity.getStackInHand(hand);
		ItemStack itemInOtherHand = livingEntity
				.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
		// passive animations
		if (itemInHand.getItem() == Items.COMPASS || itemInHand.getItem() == Items.CLOCK) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 1f, 1.5f)), -0.2f, 0.3f);
		}
		if ((itemInHand.getItem() == Items.FILLED_MAP && itemInOtherHand.isEmpty() && hand == Hand.MAIN_HAND)
				|| (itemInOtherHand.getItem() == Items.FILLED_MAP && itemInHand.isEmpty() && hand == Hand.OFF_HAND)) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 0.5f, 1.5f)), -0.4f,
					0.3f);
		}else if(itemInHand.getItem() == Items.FILLED_MAP && hand == Hand.MAIN_HAND) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 0.5f, 1.5f)), 0f,
					0.3f);
		}
		if(itemInHand.getItem() == Items.FILLED_MAP && hand == Hand.OFF_HAND) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 0.5f, 1.5f)), 0f,
					0.3f);
		}
		if(livingEntity.isSleeping()) {
			applyArmTransforms(arm, 0, 0f, 0f);
			return; // Dont try to apply more
		}
		// Stop here if the hands are doing something
		if(livingEntity.getActiveHand() == hand && livingEntity.getItemUseTime() > 0) {
			UseAction action = itemInHand.getUseAction();
			if(action == UseAction.BLOCK || action == UseAction.SPEAR || action == UseAction.BOW || action == UseAction.CROSSBOW) {
				return;// stop
			}
		}
		// active animations
		if(livingEntity.hasVehicle()) {
			if(livingEntity.getVehicle() instanceof BoatEntity) {
				BoatEntity boat = (BoatEntity) livingEntity.getVehicle();
				float paddle = boat.interpolatePaddlePhase(arm == Arm.LEFT ? 0 : 1, tick);
				applyArmTransforms(arm, -1.1f -MathHelper.sin(paddle) * 0.3f, 0.2f, 0.3f);
			}
			if(livingEntity.getVehicle() instanceof HorseEntity) {
				float rotation = -MathHelper.cos(((HorseEntity)livingEntity.getVehicle()).limbAngle * 0.3f);
				rotation *= 0.1;
				applyArmTransforms(arm, -1.1f -rotation, -0.2f, 0.3f);
			}
		}
		if (livingEntity.getActiveHand() == hand && livingEntity.getItemUseTime() > 0) {
			UseAction action = itemInHand.getUseAction();
			// Eating/Drinking
			if (action == UseAction.EAT || action == UseAction.DRINK) {
				applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 1f, 2f))
						+ MathHelper.sin(tick * 1.5f) * 0.1f, -0.3f, 0.3f);
			}
		}
	}

	private void applyArmTransforms(Arm arm, float pitch, float yaw, float roll) {
		ModelPart part = arm == Arm.RIGHT ? this.rightArm : this.leftArm;
		part.pitch = pitch;
		part.yaw = yaw;
		if (arm == Arm.LEFT) // Just mirror yaw for the left hand
			part.yaw *= -1;
		part.roll = roll;
		if (arm == Arm.LEFT)
			part.roll *= -1;
	}

}*/
