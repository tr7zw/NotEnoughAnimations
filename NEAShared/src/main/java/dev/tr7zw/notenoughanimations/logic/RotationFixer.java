package dev.tr7zw.notenoughanimations.logic;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class RotationFixer {
	
	public void onTickEnd(Player player) {
	    
//	    if (shouldFixateBody(player)) { // Bring first person blocking to the third person, fix eating/drinking
//            player.setYBodyRot(player.getYHeadRot());
//            player.yBodyRotO = player.yHeadRotO;
//        }
	}
	
//	private boolean shouldFixateBody(Player player) {
//		if (player.isPassenger())
//			return false;
//		if(NEAnimationsLoader.config.keepBodyRotatedWithHead) {
//		    return true;
//		}
//		if (NEAnimationsLoader.config.enableRotationLocking && player.isUsingItem() && !player.getUseItem().isEmpty()) {
//			ItemStack activeItem = player.getUseItem();
//			Item item = activeItem.getItem();
//			UseAnim action = item.getUseAnimation(activeItem);
//			if (action == UseAnim.BLOCK
//					|| action == UseAnim.EAT
//					|| action == UseAnim.DRINK) {
//				return item.getUseDuration(activeItem) > 0;
//			} else {
//				return false;
//			}
//		} else {
//			return false;
//		}
//	}
	
}
