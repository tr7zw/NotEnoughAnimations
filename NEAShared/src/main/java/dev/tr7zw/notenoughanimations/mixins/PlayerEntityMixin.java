package dev.tr7zw.notenoughanimations.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.renderlayer.SwordRenderLayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public class PlayerEntityMixin implements PlayerData {
	
    private int armsUpdated = 0;
    private long lastUpdate = System.currentTimeMillis();
    private float[] lastRotations = new float[12];
    private ItemStack sideSword = ItemStack.EMPTY;
    private ItemStack[] lastHeldItems = new ItemStack[2];
    private boolean disableBodyRotation = false;
    private int itemSwapAnimationTimer = 0;
    private int lastAnimationSwapTick = -1;
    
	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo info) {
		NEAnimationsLoader.INSTANCE.rotationFixer.onTickEnd((Player)(Object)this);
		updateRenderLayerItems();
	}

    @Override
    public boolean isUpdated(int frameId) {
        return armsUpdated == frameId;
    }

    @Override
    public void setUpdated(int frameId) {
        armsUpdated = frameId;
        lastUpdate = System.nanoTime();
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
    
    private void updateRenderLayerItems() {
        SwordRenderLayer.update((Player)(Object)this);
    }

    @Override
    public void disableBodyRotation(boolean val) {
        disableBodyRotation = val;
    }

    @Override
    public boolean isBodyRotationDisabled() {
        return disableBodyRotation;
    }

    @Override
    public ItemStack[] getLastHeldItems() {
        return lastHeldItems;
    }

    @Override
    public int getItemSwapAnimationTimer() {
        return itemSwapAnimationTimer;
    }

    @Override
    public void setItemSwapAnimationTimer(int count) {
        this.itemSwapAnimationTimer = count;
    }

    @Override
    public int getLastAnimationSwapTick() {
        return lastAnimationSwapTick;
    }

    @Override
    public void setLastAnimationSwapTick(int count) {
        this.lastAnimationSwapTick = count;
    }
	
}
