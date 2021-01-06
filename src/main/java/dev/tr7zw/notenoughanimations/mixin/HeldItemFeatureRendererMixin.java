package dev.tr7zw.notenoughanimations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.RenderPhaseAlternative;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayer.MultiPhaseParameters;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

/**
 * Stops items in the hand from rendering while using two handed items
 *
 */
@Mixin(HeldItemFeatureRenderer.class)
public abstract class HeldItemFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
		extends FeatureRenderer<T, M> {

	public HeldItemFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}

	private static RenderLayer getTextNoCull(Identifier texture) {
		return RenderLayer.of("text", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, 7, 256, false, true,
				MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, false, false))
						.alpha(RenderPhaseAlternative.ONE_TENTH_ALPHA)
						.transparency(RenderPhaseAlternative.TRANSLUCENT_TRANSPARENCY)
						.lightmap(RenderPhaseAlternative.ENABLE_LIGHTMAP).cull(RenderPhaseAlternative.DISABLE_CULLING)
						.build(false));
	}

	private static final RenderLayer MAP_BACKGROUND = getTextNoCull(
			(Identifier) new Identifier("textures/map/map_background.png"));
	private static final RenderLayer MAP_BACKGROUND_CHECKERBOARD = getTextNoCull(
			(Identifier) new Identifier("textures/map/map_background_checkerboard.png"));

	@Inject(at = @At("HEAD"), method = "renderItem", cancellable = true)
	private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		if(entity.isSleeping()) { // Stop holding stuff in your sleep
			info.cancel();
			return;
		}
		if(getContextModel() instanceof ModelWithArms) {
			if (arm == entity.getMainArm() && entity.getMainHandStack().getItem() == Items.FILLED_MAP) { // Mainhand with or without the offhand
				matrices.push();
				((ModelWithArms) this.getContextModel()).setArmAngle(arm, matrices);
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0f));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(200.0f));
				boolean bl = arm == Arm.LEFT;
				matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125, -0.625);
				renderFirstPersonMap(matrices, vertexConsumers, light, stack, !entity.getOffHandStack().isEmpty(), entity.getMainArm() == Arm.LEFT);
				matrices.pop();
				info.cancel();
				return;
			}
			if (arm != entity.getMainArm() && entity.getOffHandStack().getItem() == Items.FILLED_MAP) { // Only offhand
				matrices.push();
				((ModelWithArms) this.getContextModel()).setArmAngle(arm, matrices);
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0f));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(200.0f));
				boolean bl = arm == Arm.LEFT;
				matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125, -0.625);
				renderFirstPersonMap(matrices, vertexConsumers, light, stack, true, false);
				matrices.pop();
				info.cancel();
				return;
			}
		}
		
		if (entity instanceof AbstractClientPlayerEntity) {
			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) entity;
			ArmPose armPose = getArmPose(player, Hand.MAIN_HAND);
			ArmPose armPose2 = getArmPose(player, Hand.OFF_HAND);
			if (!(isUsingboothHands(armPose) || isUsingboothHands(armPose2)))
				return;
			if (armPose.method_30156()) {
				armPose2 = player.getOffHandStack().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
			}

			if (player.getMainArm() == Arm.RIGHT) {
				if (arm == Arm.RIGHT && isUsingboothHands(armPose2)) {
					info.cancel();
					return;
				} else if (arm == Arm.LEFT && isUsingboothHands(armPose)) {
					info.cancel();
					return;
				}
			} else {
				if (arm == Arm.LEFT && isUsingboothHands(armPose2)) {
					info.cancel();
					return;
				} else if (arm == Arm.RIGHT && isUsingboothHands(armPose)) {
					info.cancel();
					return;
				}
			}
		}
	}

	private boolean isUsingboothHands(ArmPose pose) {
		return pose == ArmPose.BOW_AND_ARROW || pose == ArmPose.CROSSBOW_CHARGE || pose == ArmPose.CROSSBOW_HOLD;
	}

	private static ArmPose getArmPose(AbstractClientPlayerEntity abstractClientPlayerEntity, Hand hand) {
		ItemStack itemStack = abstractClientPlayerEntity.getStackInHand(hand);
		if (itemStack.isEmpty()) {
			return ArmPose.EMPTY;
		} else {
			if (abstractClientPlayerEntity.getActiveHand() == hand
					&& abstractClientPlayerEntity.getItemUseTimeLeft() > 0) {
				UseAction useAction = itemStack.getUseAction();
				if (useAction == UseAction.BLOCK) {
					return ArmPose.BLOCK;
				}

				if (useAction == UseAction.BOW) {
					return ArmPose.BOW_AND_ARROW;
				}

				if (useAction == UseAction.SPEAR) {
					return ArmPose.THROW_SPEAR;
				}

				if (useAction == UseAction.CROSSBOW && hand == abstractClientPlayerEntity.getActiveHand()) {
					return ArmPose.CROSSBOW_CHARGE;
				}
			} else if (!abstractClientPlayerEntity.handSwinging && itemStack.getItem() == Items.CROSSBOW
					&& CrossbowItem.isCharged(itemStack)) {
				return ArmPose.CROSSBOW_HOLD;
			}

			return ArmPose.ITEM;
		}
	}

	// custom render
	private void renderFirstPersonMap(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
			ItemStack stack, boolean small, boolean lefthanded) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (small) {
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(160.0f));
			matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
			matrices.scale(0.38f, 0.38f, 0.38f);
			
			matrices.translate(-0.1, -1.2, 0.0);
			matrices.scale(0.0098125f, 0.0098125f, 0.0098125f);
		} else {
			if(lefthanded) {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(160.0f));
				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(150.0f));
				matrices.scale(0.38f, 0.38f, 0.38f);
				
				matrices.translate(+0.5, -1.3, 0.0);
			} else {
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(160.0f));
				matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(210.0f));
				matrices.scale(0.38f, 0.38f, 0.38f);
				
				matrices.translate(-1.0, -1.8, 0.0);
			}

			matrices.scale(0.0138125f, 0.0138125f, 0.0138125f);
		}
		MapState mapState = FilledMapItem.getOrCreateMapState((ItemStack) stack, (World) client.world);
		VertexConsumer vertexConsumer = vertexConsumers
				.getBuffer(mapState == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
		Matrix4f matrix4f = matrices.peek().getModel();
		vertexConsumer.vertex(matrix4f, -7.0f, 135.0f, 0.0f).color(255, 255, 255, 255).texture(0.0f, 1.0f).light(light)
				.next();
		vertexConsumer.vertex(matrix4f, 135.0f, 135.0f, 0.0f).color(255, 255, 255, 255).texture(1.0f, 1.0f).light(light)
				.next();
		vertexConsumer.vertex(matrix4f, 135.0f, -7.0f, 0.0f).color(255, 255, 255, 255).texture(1.0f, 0.0f).light(light)
				.next();
		vertexConsumer.vertex(matrix4f, -7.0f, -7.0f, 0.0f).color(255, 255, 255, 255).texture(0.0f, 0.0f).light(light)
				.next();
		if (mapState != null) {
			client.gameRenderer.getMapRenderer().draw(matrices, vertexConsumers, mapState, false, light);
		}
	}

}
