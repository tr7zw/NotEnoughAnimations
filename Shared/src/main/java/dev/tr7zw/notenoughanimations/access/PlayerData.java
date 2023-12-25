package dev.tr7zw.notenoughanimations.access;

import java.util.function.Supplier;

import dev.tr7zw.notenoughanimations.animations.DataHolder;
import net.minecraft.world.entity.Pose;
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

    public void setPoseOverwrite(Pose state);

    public Pose getPoseOverwrite();

    public <T> T getData(DataHolder<T> holder, Supplier<T> builder);

}
