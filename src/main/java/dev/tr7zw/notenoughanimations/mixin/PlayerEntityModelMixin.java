package dev.tr7zw.notenoughanimations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
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
	public void setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo info) {
		boolean rightHanded = livingEntity.getMainArm() == Arm.RIGHT;
		applyAnimations(livingEntity, Arm.RIGHT, rightHanded ? Hand.MAIN_HAND : Hand.OFF_HAND, h);
		applyAnimations(livingEntity, Arm.LEFT, !rightHanded ? Hand.MAIN_HAND : Hand.OFF_HAND, h);
	}

	private void applyAnimations(T livingEntity, Arm arm, Hand hand, float tick) {
		ItemStack itemInHand = livingEntity.getStackInHand(hand);
		ItemStack itemInOtherHand = livingEntity
				.getStackInHand(hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND);
		// passive animations
		if (itemInHand.getItem() == Items.COMPASS) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 1f, 1.5f)), -0.2f, 0.3f);
		}
		if ((itemInHand.getItem() == Items.FILLED_MAP && itemInOtherHand.isEmpty() && hand == Hand.MAIN_HAND)
				|| (itemInOtherHand.getItem() == Items.FILLED_MAP && itemInHand.isEmpty() && hand == Hand.OFF_HAND)) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 0.5f, 1.5f)), -0.4f,
					0.3f);
		}
		if(itemInHand.getItem() == Items.FILLED_MAP && hand == Hand.OFF_HAND) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.pitch - 90f) / 180f, 0.5f, 1.5f)), -0.2f,
					0.3f);
		}
		// active animations
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
	}

}
