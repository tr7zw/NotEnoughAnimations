package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public class PlayerEntityMixin implements PlayerData {
	
    private int armsUpdated = 0;
    private long lastUpdate = System.currentTimeMillis();
    private float[] lastRotations = new float[6];
    private ItemStack sideSword = ItemStack.EMPTY;
    
	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo info) {
		NEAnimationsLoader.INSTANCE.rotationFixer.onTickEnd((Player)(Object)this);
	}

    @Override
    public boolean isUpdated(int frameId) {
        return armsUpdated == frameId;
    }

    @Override
    public void setUpdated(int frameId) {
        armsUpdated = frameId;
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public long lastUpdate() {
        return lastUpdate;
    }

    @Override
    public float[] getLastRotations() {
        return lastRotations;
    }

    @Override
    public ItemStack getSideSword() {
        return sideSword;
    }

    @Override
    public void setSideSword(ItemStack item) {
        this.sideSword = item;
    }
	
}
