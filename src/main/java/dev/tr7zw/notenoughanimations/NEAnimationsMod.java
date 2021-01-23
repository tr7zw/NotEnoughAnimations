package dev.tr7zw.notenoughanimations;

import dev.tr7zw.transliterationlib.api.event.PlayerEvents;
import dev.tr7zw.transliterationlib.api.wrapper.entity.Player;
import dev.tr7zw.transliterationlib.api.wrapper.item.Item;
import dev.tr7zw.transliterationlib.api.wrapper.item.ItemStack;
import dev.tr7zw.transliterationlib.api.wrapper.item.UseAction;
import net.fabricmc.api.ModInitializer;

public class NEAnimationsMod implements ModInitializer {
	@Override
	public void onInitialize() {
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
		if (player.isUsingItem() && !player.getActiveItemStack().isEmpty()) {
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
