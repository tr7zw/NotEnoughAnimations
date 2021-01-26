package dev.tr7zw.notenoughanimations;

import static dev.tr7zw.transliterationlib.api.TRansliterationLib.transliteration;

import dev.tr7zw.transliterationlib.api.event.RenderEvent;
import dev.tr7zw.transliterationlib.api.wrapper.entity.ClientPlayer;
import dev.tr7zw.transliterationlib.api.wrapper.item.ArmPose;
import dev.tr7zw.transliterationlib.api.wrapper.item.Hand;
import dev.tr7zw.transliterationlib.api.wrapper.item.Item;
import dev.tr7zw.transliterationlib.api.wrapper.item.ItemStack;
import dev.tr7zw.transliterationlib.api.wrapper.item.UseAction;
import dev.tr7zw.transliterationlib.api.wrapper.model.ModelWithArms;

public class HeldItemHandler {

	private Item filledMap = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "filled_map"));
	private Item crossbow = transliteration.getEnumWrapper().getItems().getItem(transliteration.constructors().newIdentifier("minecraft", "crossbow"));
	
	public void enable() {
		RenderEvent.RENDER_HELD_ITEM.register((entity, model, stack, arm, matrices, vertexConsumers, light, info) -> {
			if(entity.isSleeping()) { // Stop holding stuff in your sleep
				info.cancel();
				return;
			}
			if(model instanceof ModelWithArms) {
				if (arm == entity.getMainArm() && entity.getMainHandStack().getItem().equals(filledMap)) { // Mainhand with or without the offhand
					matrices.push();
					((ModelWithArms) model).setArmAngle(arm, matrices);
					matrices.multiply(transliteration.singletonWrapper().getVector3f().getPositiveX().getDegreesQuaternion(-90.0f));
					matrices.multiply(transliteration.singletonWrapper().getVector3f().getPositiveY().getDegreesQuaternion(200.0f));
					boolean bl = arm.equals(arm.getLeft());
					matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125, -0.625);
					transliteration.getWrapper().renderFirstPersonMap(matrices, vertexConsumers, light, stack, !entity.getOffHandStack().isEmpty(), entity.getMainArm().equals(arm.getLeft()));
					matrices.pop();
					info.cancel();
					return;
				}
				if (arm != entity.getMainArm() && entity.getOffHandStack().getItem().equals(filledMap)) { // Only offhand
					matrices.push();
					((ModelWithArms) model).setArmAngle(arm, matrices);
					matrices.multiply(transliteration.singletonWrapper().getVector3f().getPositiveX().getDegreesQuaternion(-90.0f));
					matrices.multiply(transliteration.singletonWrapper().getVector3f().getPositiveY().getDegreesQuaternion(200.0f));
					boolean bl = arm.equals(arm.getLeft());
					matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125, -0.625);
					transliteration.getWrapper().renderFirstPersonMap(matrices, vertexConsumers, light, stack, true, false);
					matrices.pop();
					info.cancel();
					return;
				}
			}
			
			if (entity instanceof ClientPlayer) {
				ClientPlayer player = (ClientPlayer) entity;
				ArmPose armPose = getArmPose(player, transliteration.getEnumWrapper().getHand().getMainHand());
				ArmPose armPose2 = getArmPose(player, transliteration.getEnumWrapper().getHand().getOffHand());
				if (!(isUsingboothHands(armPose) || isUsingboothHands(armPose2)))
					return;
				if (armPose.isTwoHanded()) {
					armPose2 = player.getOffHandStack().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
				}

				if (player.getMainArm().equals(arm.getRight())) {
					if (arm.equals(arm.getRight()) && isUsingboothHands(armPose2)) {
						info.cancel();
						return;
					} else if (arm.equals(arm.getLeft()) && isUsingboothHands(armPose)) {
						info.cancel();
						return;
					}
				} else {
					if (arm.equals(arm.getLeft()) && isUsingboothHands(armPose2)) {
						info.cancel();
						return;
					} else if (arm.equals(arm.getRight()) && isUsingboothHands(armPose)) {
						info.cancel();
						return;
					}
				}
			}
		});
	}
	

	private boolean isUsingboothHands(ArmPose pose) {
		return pose == ArmPose.BOW_AND_ARROW || pose == ArmPose.CROSSBOW_CHARGE || pose == ArmPose.CROSSBOW_HOLD;
	}

	private ArmPose getArmPose(ClientPlayer abstractClientPlayerEntity, Hand hand) {
		ItemStack itemStack = abstractClientPlayerEntity.getStackInHand(hand);
		if (itemStack.isEmpty()) {
			return ArmPose.EMPTY;
		} else {
			if (abstractClientPlayerEntity.getActiveHand() == hand
					&& abstractClientPlayerEntity.getItemUseTime() > 0) {
				UseAction useAction = itemStack.getUseAction();
				if (useAction.equals(useAction.getBlock())) {
					return ArmPose.BLOCK;
				}

				if (useAction.equals(useAction.getBow())) {
					return ArmPose.BOW_AND_ARROW;
				}

				if (useAction.equals(useAction.getSpear())) {
					return ArmPose.THROW_SPEAR;
				}

				if (useAction.equals(useAction.getCrossbow()) && hand.equals(abstractClientPlayerEntity.getActiveHand())) {
					return ArmPose.CROSSBOW_CHARGE;
				}
			} else if (!abstractClientPlayerEntity.isHandSwinging() && itemStack.getItem().equals(crossbow)
					&& transliteration.getWrapper().isChargedCrossbow(itemStack)) {
				return ArmPose.CROSSBOW_HOLD;
			}

			return ArmPose.ITEM;
		}
	}
	
	
}
