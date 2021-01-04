package dev.tr7zw.notenoughanimations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo info) {
		if(isBlockingFast()) { // Bring first person blocking to the third person
			this.bodyYaw = headYaw;
			this.prevBodyYaw = prevHeadYaw;
		}
	}
	
	public boolean isBlockingFast() {
		if (this.isUsingItem() && !this.activeItemStack.isEmpty()) {
			Item item = this.activeItemStack.getItem();
			if (item.getUseAction(this.activeItemStack) != UseAction.BLOCK) {
				return false;
			} else {
				return item.getMaxUseTime(this.activeItemStack) > 0;
			}
		} else {
			return false;
		}
	}
	
}
