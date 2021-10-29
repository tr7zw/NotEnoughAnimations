package dev.tr7zw.notenoughanimations.access;

import net.minecraft.world.item.ItemStack;

public interface PlayerData {

    public boolean isUpdated(int frameId);
    
    public void setUpdated(int frameId);
    
    public long lastUpdate();
    
    public float[] getLastRotations();
    
    public ItemStack getSideSword();
    
    public void setSideSword(ItemStack item);
    
    public ItemStack[] getBackTools();
    
}
