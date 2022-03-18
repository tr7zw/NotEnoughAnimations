package dev.tr7zw.notenoughanimations.access;

import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.world.item.ItemStack;

public interface PlayerData {

    public boolean isUpdated(int frameId);
    
    public void setUpdated(int frameId);
    
    public long lastUpdate();
    
    public float[] getLastRotations();
    
    public ItemStack getSideSword();
    
    public void setSideSword(ItemStack item);
    
    public void disableBodyRotation(boolean val);
    
    public boolean isBodyRotationDisabled();
    
    public ItemStack[] getLastHeldItems();
    
    public int getItemSwapAnimationTimer();
    
    public void setItemSwapAnimationTimer(int count);
    
    public int getLastAnimationSwapTick();
    
    public void setLastAnimationSwapTick(int count);
    
}
