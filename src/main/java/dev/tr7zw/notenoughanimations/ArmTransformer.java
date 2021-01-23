package dev.tr7zw.notenoughanimations;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import dev.tr7zw.transliterationlib.api.event.RenderEvent;
import dev.tr7zw.transliterationlib.api.util.MathHelper;
import dev.tr7zw.transliterationlib.api.wrapper.entity.LivingEntity;
import dev.tr7zw.transliterationlib.api.wrapper.item.Arm;
import dev.tr7zw.transliterationlib.api.wrapper.item.Hand;
import dev.tr7zw.transliterationlib.api.wrapper.item.Item;
import dev.tr7zw.transliterationlib.api.wrapper.item.ItemStack;
import dev.tr7zw.transliterationlib.api.wrapper.item.Items;
import dev.tr7zw.transliterationlib.api.wrapper.item.UseAction;

public class ArmTransformer {

	private Item clock = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "clock"));
	private Item compass = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "compass"));
	private Item filledMap = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "filled_map"));
	
	public void enable() {
		Arm arm = transliteration.getEnumWrapper().getArm();
		Hand hand = transliteration.getEnumWrapper().getHand();
		
		RenderEvent.SET_ANGLES_END.register((entity, tick, info) -> {
			boolean rightHanded = entity.getMainArm() == arm.getRight();
			applyAnimations(entity, arm.getRight(), rightHanded ? hand.getMainHand() : hand.getOffHand(), tick);
			applyAnimations(entity, arm.getLeft(), !rightHanded ? hand.getMainHand() : hand.getOffHand(), tick);
		});
	}
	
	private void applyAnimations(LivingEntity livingEntity, Arm arm, Hand hand, float tick) {
		ItemStack itemInHand = livingEntity.getStackInHand(hand);
		ItemStack itemInOtherHand = livingEntity
				.getStackInHand(hand == hand.getMainHand() ? hand.getOffHand() : hand.getMainHand());
		// passive animations
		if (itemInHand.getItem().equals(compass) || itemInHand.getItem().equals(clock)) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 1f, 1.5f)), -0.2f, 0.3f);
		}
		if ((itemInHand.getItem().equals(filledMap) && itemInOtherHand.isEmpty() && hand == hand.getMainHand())
				|| (itemInOtherHand.getItem().equals(filledMap) && itemInHand.isEmpty() && hand == hand.getOffHand())) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), -0.4f,
					0.3f);
		}else if(itemInHand.getItem().equals(filledMap) && hand == hand.getMainHand()) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), 0f,
					0.3f);
		}
		if(itemInHand.getItem().equals(filledMap) && hand == hand.getOffHand()) {
			applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), 0f,
					0.3f);
		}
		if(livingEntity.isSleeping()) {
			applyArmTransforms(arm, 0, 0f, 0f);
			return; // Dont try to apply more
		}
		// Stop here if the hands are doing something
		if(livingEntity.getActiveHand() == hand && livingEntity.getItemUseTime() > 0) {
			UseAction action = itemInHand.getUseAction();
			if(action == action.getBlock() || action == action.getSpear() || action == action.getBow() || action == action.getCrossbow()) {
				return;// stop
			}
		}
		// active animations
		if(livingEntity.hasVehicle()) {
			if(livingEntity.getVehicle() instanceof BoatEntity) {
				BoatEntity boat = (BoatEntity) livingEntity.getVehicle();
				float paddle = boat.interpolatePaddlePhase(arm == arm.getLeft() ? 0 : 1, tick);
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
			if (action == action.getEat() || action == action.getDrink()) {
				applyArmTransforms(arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 1f, 2f))
						+ MathHelper.sin(tick * 1.5f) * 0.1f, -0.3f, 0.3f);
			}
		}
	}

	private void applyArmTransforms(Arm arm, float pitch, float yaw, float roll) {
		ModelPart part = arm == arm.getRight() ? this.rightArm : this.leftArm;
		part.pitch = pitch;
		part.yaw = yaw;
		if (arm == arm.getLeft()) // Just mirror yaw for the left hand
			part.yaw *= -1;
		part.roll = roll;
		if (arm == arm.getLeft())
			part.roll *= -1;
	}
	
}
