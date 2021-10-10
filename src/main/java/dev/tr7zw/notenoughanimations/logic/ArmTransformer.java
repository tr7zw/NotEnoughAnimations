package dev.tr7zw.notenoughanimations.logic;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.transliterationlib.api.event.RenderEvent;
import dev.tr7zw.transliterationlib.api.util.MathHelper;
import dev.tr7zw.transliterationlib.api.wrapper.entity.BoatEntity;
import dev.tr7zw.transliterationlib.api.wrapper.entity.HorseEntity;
import dev.tr7zw.transliterationlib.api.wrapper.entity.LivingEntity;
import dev.tr7zw.transliterationlib.api.wrapper.entity.Player;
import dev.tr7zw.transliterationlib.api.wrapper.item.Arm;
import dev.tr7zw.transliterationlib.api.wrapper.item.Hand;
import dev.tr7zw.transliterationlib.api.wrapper.item.Item;
import dev.tr7zw.transliterationlib.api.wrapper.item.ItemStack;
import dev.tr7zw.transliterationlib.api.wrapper.item.UseAction;
import dev.tr7zw.transliterationlib.api.wrapper.model.ModelPart;
import dev.tr7zw.transliterationlib.api.wrapper.model.PlayerEntityModel;

public class ArmTransformer {

	private Set<Item> compatibleMaps = new HashSet<>();
	private Set<Item> holdingItems = new HashSet<>();
	
	private Map<Integer, float[]> lastRotations = new HashMap<>();
	private Map<Integer, Long> lastUpdate = new HashMap<>();
	private Set<Integer> updated = new HashSet<>();
	private boolean doneLatebind = false;
	
	public void enable() {
	    
		Arm arm = transliteration.getEnumWrapper().getArm();
		Hand hand = transliteration.getEnumWrapper().getHand();
		
		RenderEvent.SET_ANGLES_END.register((entity, model, tick, info) -> {
		    if(!doneLatebind)lateBind();
		    if(transliteration.getMinecraftClient().getWorld().isNull()) { // We are in a main menu or something
		        return;
		    }
		    if(entity instanceof Player && ((Player)entity).isCrawling()) { // Crawling has its own animations and messing with it screws stuff up
		        return;
		    }
			boolean rightHanded = entity.getMainArm() == arm.getRight();
			applyAnimations(entity, model, arm.getRight(), rightHanded ? hand.getMainHand() : hand.getOffHand(), tick);
			applyAnimations(entity, model, arm.getLeft(), !rightHanded ? hand.getMainHand() : hand.getOffHand(), tick);
			
			
			if(NEAnimationsLoader.config.enableAnimationSmoothing) {
    			int id = entity.getId();
    			float[] last = lastRotations.computeIfAbsent(id, i -> new float[6]);
    			boolean differentFrame = updated.add(id);
    			long timePassed = System.currentTimeMillis() - lastUpdate.getOrDefault(id, 0l);
    			if(timePassed < 1)
    				timePassed = 1;
    			interpolate(model.getLeftArm(), last, 0, timePassed, differentFrame, NEAnimationsLoader.config.animationSmoothingSpeed);
    			interpolate(model.getRightArm(), last, 3, timePassed, differentFrame, NEAnimationsLoader.config.animationSmoothingSpeed);
    			if(differentFrame)
    			    lastUpdate.put(id, System.currentTimeMillis());
			}
		});
		
		RenderEvent.START_RENDER.register(() -> {
		    updated.clear();
		});
	}
	
	private void interpolate(ModelPart model, float[] last, int offset, long timePassed, boolean differentFrame, float speed) {
		if(!differentFrame) { //Rerendering the place in the same frame
			model.setPitch(last[offset]);
			model.setYaw(last[offset+1]);
			model.setRoll(last[offset+2]);
			return;
		}
		if(timePassed > 200) { //Don't try to interpolate states older than 200ms
			last[offset] = model.getPitch();
			last[offset+1] = model.getYaw();
			last[offset+2] = model.getRoll();
			cleanInvalidData(last, offset);
			return;
		}
		float amount = ((1f/(1000f/timePassed)))*speed;
		if(amount > 1)amount = 1;
		last[offset] = last[offset]+((model.getPitch()-last[offset])*amount);
		last[offset+1] = last[offset+1]+((wrapDegrees(model.getYaw())-wrapDegrees(last[offset+1]))*amount);
		last[offset+2] = last[offset+2]+((model.getRoll()-last[offset+2])*amount);
		cleanInvalidData(last, offset);
		model.setPitch(last[offset]);
		model.setYaw(last[offset+1]);
		model.setRoll(last[offset+2]);
	}
	
	/**
	 * When using a quickcharge 5 crossbow it is able to cause NaN values to show up because of how broken it is.
	 * 
	 * @param data
	 * @param offset
	 */
	private void cleanInvalidData(float[] data, int offset) {
	    if(Float.isNaN(data[offset]))data[offset] = 0;
	    if(Float.isNaN(data[offset+1]))data[offset+1] = 0;
	    if(Float.isNaN(data[offset+2]))data[offset+2] = 0;
	}
	
	private void applyAnimations(LivingEntity livingEntity, PlayerEntityModel model, Arm arm, Hand hand, float tick) {
		ItemStack itemInHand = livingEntity.getStackInHand(hand);
		ItemStack itemInOtherHand = livingEntity
				.getStackInHand(hand == hand.getMainHand() ? hand.getOffHand() : hand.getMainHand());
		// passive animations
		if (holdingItems.contains(itemInHand.getItem())) {
			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 1f, 1.5f)), -0.2f, 0.3f);
		}
		if(NEAnimationsLoader.config.enableInWorldMapRendering) {
    		if ((compatibleMaps.contains(itemInHand.getItem()) && itemInOtherHand.isEmpty() && hand == hand.getMainHand())
    				|| compatibleMaps.contains(itemInOtherHand.getItem()) && itemInHand.isEmpty() && hand == hand.getOffHand()) {
    			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), -0.4f,
    					0.3f);
    		}else if(compatibleMaps.contains(itemInHand.getItem()) && hand == hand.getMainHand()) {
    			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), 0f,
    					0.3f);
    		}
    		if(compatibleMaps.contains(itemInHand.getItem()) && hand == hand.getOffHand()) {
    			applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 0.5f, 1.5f)), 0f,
    					0.3f);
    		}
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
			if(livingEntity.getVehicle() instanceof BoatEntity && NEAnimationsLoader.config.enableRowBoatAnimation) {
				BoatEntity boat = (BoatEntity) livingEntity.getVehicle();
				float paddle = boat.interpolatePaddlePhase(arm == arm.getLeft() ? 0 : 1, tick);
				applyArmTransforms(model, arm, -1.1f -MathHelper.sin(paddle) * 0.3f, 0.2f, 0.3f);
			}
			if(livingEntity.getVehicle() instanceof HorseEntity && NEAnimationsLoader.config.enableHorseAnimation) {
				float rotation = -MathHelper.cos(((HorseEntity)livingEntity.getVehicle()).getLimbAngle() * 0.3f);
				rotation *= 0.1;
				applyArmTransforms(model, arm, -1.1f -rotation, -0.2f, 0.3f);
			}
		}
		if(livingEntity.isClimbing() && NEAnimationsLoader.config.enableLadderAnimation) {
			float rotation = -MathHelper.cos((float) (livingEntity.getPos().getY()*2));
			rotation *= 0.3;
			if(arm.equals(arm.getLeft()))rotation *= -1;
			applyArmTransforms(model, arm, -1.1f -rotation, -0.2f, 0.3f);
		}
		if (livingEntity.getActiveHand() == hand && livingEntity.getItemUseTime() > 0  && NEAnimationsLoader.config.enableEatDrinkAnimation) {
			UseAction action = itemInHand.getUseAction();
			// Eating/Drinking
			if (action.equals(action.getEat()) || action.equals(action.getDrink())) {
				applyArmTransforms(model, arm, -(MathHelper.lerp(-1f * (livingEntity.getPitch() - 90f) / 180f, 1f, 2f))
						+ MathHelper.sin(tick * 1.5f) * 0.1f, -0.3f, 0.3f);
			}
		}
	}
	
	private void lateBind() {
	       Item invalid = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "air"));
	        for(String itemId : NEAnimationsLoader.config.holdingItems) {
	            try {
	                Item item = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier(itemId.split(":")[0], itemId.split(":")[1]));
	                if(invalid.getHandler() != item.getHandler())
	                    holdingItems.add(item);
	            }catch(Exception ex) {
	                System.err.println("Unknown item to add to the holding list: " + itemId);
	            }
	        }
	        compatibleMaps.add(transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "filled_map")));
	        Item antiqueAtlas = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("antiqueatlas", "antique_atlas"));
	        if(invalid.getHandler() != antiqueAtlas.getHandler()) {
	            compatibleMaps.add(antiqueAtlas);
	            System.out.println("Added AntiqueAtlas support to Not Enough Animations!");
	        }
	    doneLatebind = true;
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
