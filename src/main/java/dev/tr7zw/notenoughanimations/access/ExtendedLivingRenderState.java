package dev.tr7zw.notenoughanimations.access;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;

public interface ExtendedLivingRenderState {

    void setEntity(LivingEntity entity);

    LivingEntity getEntity();
}
