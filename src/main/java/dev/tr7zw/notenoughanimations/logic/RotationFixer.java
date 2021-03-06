package dev.tr7zw.notenoughanimations.logic;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.transliterationlib.api.event.PlayerEvents;
import dev.tr7zw.transliterationlib.api.wrapper.entity.Player;
import dev.tr7zw.transliterationlib.api.wrapper.item.Item;
import dev.tr7zw.transliterationlib.api.wrapper.item.ItemStack;
import dev.tr7zw.transliterationlib.api.wrapper.item.UseAction;

public class RotationFixer {

	public void enable() {
		PlayerEvents.PLAYER_TICK_END.register((player, info) -> {
			if (shouldFixateBody(player)) { // Bring first person blocking to the third person, fix eating/drinking
				player.setBodyYaw(player.getHeadYaw());
				player.setPrevBodyYaw(player.getPrevHeadYaw());
			}
		});
	}
	
	private boolean shouldFixateBody(Player player) {
		if (player.hasVehicle())
			return false;
		if (NEAnimationsLoader.config.enableRotationLocking && player.isUsingItem() && !player.getActiveItemStack().isEmpty()) {
			ItemStack activeItem = player.getActiveItemStack();
			Item item = activeItem.getItem();
			UseAction action = item.getUseAction(activeItem);
			if (action.equals(action.getBlock())
					|| action.equals(action.getEat())
					|| action.equals(action.getDrink())) {
				return item.getMaxUseTime(activeItem) > 0;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
