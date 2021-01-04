package dev.tr7zw.notenoughanimations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo info) {
		if(shouldFixateBody()) { // Bring first person blocking to the third person, fix eating/drinking
			this.bodyYaw = headYaw;
			this.prevBodyYaw = prevHeadYaw;
		}
	}
	
	public boolean shouldFixateBody() {
		if(this.getMainHandStack().getItem() == Items.COMPASS || this.getOffHandStack().getItem() == Items.COMPASS)return true;
		if (this.isUsingItem() && !this.activeItemStack.isEmpty()) {
			Item item = this.activeItemStack.getItem();
			UseAction action = item.getUseAction(this.activeItemStack);
			if (action == UseAction.BLOCK || action == UseAction.EAT || action == UseAction.DRINK) {
				return item.getMaxUseTime(this.activeItemStack) > 0;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
