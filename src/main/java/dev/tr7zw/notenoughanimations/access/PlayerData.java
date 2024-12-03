package dev.tr7zw.notenoughanimations.access;

import java.util.function.Supplier;

import dev.tr7zw.notenoughanimations.versionless.animations.DataHolder;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;

public interface PlayerData {

    public int isUpdated(int frameId);

    public void setUpdated(int frameId);

    public float[] getLastRotations();

    public ItemStack getSideSword();

    public void setSideSword(ItemStack item);

    /**
     * Disables rotationlocking, so the animation has full controll
     * 
     * @param val
     */
    public void setDisableBodyRotation(boolean val);

    public boolean isDisableBodyRotation();

    /**
     * Overwrites rotationlocking to be active
     * 
     * @param val
     */
    public void setRotateBodyToHead(boolean val);

    public boolean isRotateBodyToHead();

    public ItemStack[] getLastHeldItems();

    public int getItemSwapAnimationTimer();

    public void setItemSwapAnimationTimer(int count);

    public int getLastAnimationSwapTick();

    public void setLastAnimationSwapTick(int count);

    public void setPoseOverwrite(Pose state);

    public Pose getPoseOverwrite();

    public <T> T getData(DataHolder<T> holder, Supplier<T> builder);

}
