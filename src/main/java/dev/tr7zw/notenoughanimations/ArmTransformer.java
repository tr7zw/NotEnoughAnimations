package dev.tr7zw.notenoughanimations;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import java.util.HashMap;
import java.util.Map;

import dev.tr7zw.transliterationlib.api.event.RenderEvent;
import dev.tr7zw.transliterationlib.api.util.MathHelper;
import dev.tr7zw.transliterationlib.api.wrapper.entity.BoatEntity;
import dev.tr7zw.transliterationlib.api.wrapper.entity.HorseEntity;
import dev.tr7zw.transliterationlib.api.wrapper.entity.LivingEntity;
import dev.tr7zw.transliterationlib.api.wrapper.item.Arm;
import dev.tr7zw.transliterationlib.api.wrapper.item.Hand;
import dev.tr7zw.transliterationlib.api.wrapper.item.Item;
import dev.tr7zw.transliterationlib.api.wrapper.item.ItemStack;
import dev.tr7zw.transliterationlib.api.wrapper.item.UseAction;
import dev.tr7zw.transliterationlib.api.wrapper.model.ModelPart;
import dev.tr7zw.transliterationlib.api.wrapper.model.PlayerEntityModel;

public class ArmTransformer {

	private Item clock = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "clock"));
	private Item compass = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "compass"));
	private Item filledMap = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "filled_map"));
	private Item torch = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "torch"));
	
	private Map<Integer, float[]> lastRotations = new HashMap<>();
	private Map<Integer, Long> lastUpdate = new HashMap<>();
	//private Map<Integer, Integer> lastTick = new HashMap<>();
	private final float animationSpeed = 6f;
	
	public void enable() {
		Arm arm = transliteration.getEnumWrapper().getArm();
		Hand hand = transliteration.getEnumWrapper().getHand();
		
		RenderEvent.SET_ANGLES_END.register((entity, model, tick, info) -> {
			boolean rightHanded = entity.getMainArm() == arm.getRight();
			applyAnimations(entity, model, arm.getRight(), rightHanded ? hand.getMainHand() : hand.getOffHand(), tick);
			applyAnimations(entity, model, arm.getLeft(), !rightHanded ? hand.getMainHand() : hand.getOffHand(), tick);
			
			int id = entity.getId();
			float[] last = lastRotations.computeIfAbsent(id, i -> new float[6]);
			boolean differentFrame = true;//entity.getAge() != lastTick.getOrDefault(id, 0); //TODO: check if this is a new frame, not the same frame
			long timePassed = System.currentTimeMillis() - lastUpdate.getOrDefault(id, System.currentTimeMillis());
			if(timePassed < 1)
				timePassed = 1;
			boolean swordInLeftHand = entity.getStackInHand(rightHanded?hand.getOffHand():hand.getMainHand()).getItem().getKeyPath().contains("sword"); // making sure modded sword also are fast. Maybe expand to other tools too?
			boolean swordInRightHand = entity.getStackInHand(rightHanded?hand.getMainHand():hand.getOffHand()).getItem().getKeyPath().contains("sword");
			interpolate(model.getLeftArm(), last, 0, timePassed, differentFrame, animationSpeed * (swordInLeftHand ? 5 : 1));
			interpolate(model.getRightArm(), last, 3, timePassed, differentFrame, animationSpeed * (swordInRightHand ? 5 : 1));
			lastUpdate.put(id, System.currentTimeMillis());
			/*if(differentFrame) {
				lastTick.put(id, entity.getAge());
			}*/
		});
	}
	
	private void interpolate(ModelPart model, float[] last, int offset, long timeScale, boolean differentFrame, float speed) {
		if(!differentFrame) { //Rerendering the place in the same frame
			model.setPitch(last[offset]);
			model.setYaw(last[offset+1]);
			model.setRoll(last[offset+2]);
			return;
		}
		last[offset] = last[offset]+((model.getPitch()-last[offset])*((1f/(1000f/timeScale)))*speed);
		last[offset+1] = last[offset+1]+((wrapDegrees(model.getYaw())-wrapDegrees(last[offset+1]))*((1f/(1000f/timeScale)))*speed);
		last[offset+2] = last[offset+2]+((model.getRoll()-last[offset+2])*((1f/(1000f/timeScale)))*speed);
		model.setPitch(last[offset]);
		model.setYaw(last[offset+1]);
		model.setRoll(last[offset+2]);
	}
	
	private void applyAnimations(LivingEntity livingEntity, PlayerEntityModel model, Arm arm, Hand hand, float tick) {
		ItemStack itemInHand = livingEntity.getStackInHand(hand);
		ItemStack itemInOtherHand = livingEntity
				.getStackInHand(hand == hand.getMainHand() ? hand.getOffHand() : hand.getMainHand());
		// passive animations
		if (itemInHand.getItem().equals(compass) || itemInHand.getItem().equals(clock) || itemInHand.getItem().equals(torch)) {
			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 1f, 1.5f)), -0.2f, 0.3f);
		}
		if ((itemInHand.getItem().equals(filledMap) && itemInOtherHand.isEmpty() && hand == hand.getMainHand())
				|| (itemInOtherHand.getItem().equals(filledMap) && itemInHand.isEmpty() && hand == hand.getOffHand())) {
			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), -0.4f,
					0.3f);
		}else if(itemInHand.getItem().equals(filledMap) && hand == hand.getMainHand()) {
			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), 0f,
					0.3f);
		}
		if(itemInHand.getItem().equals(filledMap) && hand == hand.getOffHand()) {
			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), 0f,
					0.3f);
		}
		if(livingEntity.isSleeping()) {
			applyArmTransforms(model, arm, 0, 0f, 0f);
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
				applyArmTransforms(model, arm, -1.1f -MathHelper.sin(paddle) * 0.3f, 0.2f, 0.3f);
			}
			if(livingEntity.getVehicle() instanceof HorseEntity) {
				float rotation = -MathHelper.cos(((HorseEntity)livingEntity.getVehicle()).getLimbAngle() * 0.3f);
				rotation *= 0.1;
				applyArmTransforms(model, arm, -1.1f -rotation, -0.2f, 0.3f);
			}
		}
		if(livingEntity.isClimbing()) {
			float rotation = -MathHelper.cos((float) (livingEntity.getPos().getY()*2));
			rotation *= 0.3;
			if(arm.equals(arm.getLeft()))rotation *= -1;
			applyArmTransforms(model, arm, -1.1f -rotation, -0.2f, 0.3f);
		}
		if (livingEntity.getActiveHand() == hand && livingEntity.getItemUseTime() > 0) {
			UseAction action = itemInHand.getUseAction();
			// Eating/Drinking
			if (action.equals(action.getEat()) || action.equals(action.getDrink())) {
				applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 1f, 2f))
						+ MathHelper.sin(tick * 1.5f) * 0.1f, -0.3f, 0.3f);
			}
		}
	}

	private void applyArmTransforms(PlayerEntityModel model, Arm arm, float pitch, float yaw, float roll) {
		ModelPart part = arm == arm.getRight() ? model.getRightArm() : model.getLeftArm();
		part.setPitch(pitch);
		part.setYaw(yaw);
		if (arm == arm.getLeft()) // Just mirror yaw for the left hand
			part.setYaw(part.getYaw() * -1);
		part.setRoll(roll);
		if (arm == arm.getLeft())
			part.setRoll(part.getRoll() * -1);
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
