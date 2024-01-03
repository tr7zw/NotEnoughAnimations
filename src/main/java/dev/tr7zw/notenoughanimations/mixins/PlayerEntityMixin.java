package dev.tr7zw.notenoughanimations.mixins;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.logic.PlayerTransformer;
import dev.tr7zw.notenoughanimations.renderlayer.SwordRenderLayer;
import dev.tr7zw.notenoughanimations.versionless.animations.DataHolder;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public class PlayerEntityMixin implements PlayerData {

    private int armsUpdated = 0;
    private float[] lastRotations = new float[PlayerTransformer.ENTRY_AMOUNT * PlayerTransformer.ENTRY_SIZE];
    private ItemStack sideSword = ItemStack.EMPTY;
    private ItemStack[] lastHeldItems = new ItemStack[2];
    private boolean disableBodyRotation = false;
    private int itemSwapAnimationTimer = 0;
    private int lastAnimationSwapTick = -1;
    private Pose poseOverwrite = null;
    private Map<DataHolder<?>, Object> animationData = new HashMap<>();

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo info) {
        updateRenderLayerItems();
    }

    @Override
    public int isUpdated(int frameId) {
        return Math.abs(frameId - armsUpdated);
    }

    @Override
    public void setUpdated(int frameId) {
        armsUpdated = frameId;
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
        SwordRenderLayer.update((Player) (Object) this);
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

    @Override
    public void setPoseOverwrite(Pose state) {
        this.poseOverwrite = state;
    }

    @Override
    public Pose getPoseOverwrite() {
        return poseOverwrite;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getData(DataHolder<T> holder, Supplier<T> builder) {
        return (T) animationData.computeIfAbsent(holder, (h) -> builder.get());
    }

}
