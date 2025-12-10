package dev.tr7zw.notenoughanimations.api;

import dev.tr7zw.notenoughanimations.access.PlayerData;
//? if >= 1.21.11 {

import net.minecraft.client.model.player.*;
//? } else {
/*
import net.minecraft.client.model.*;
*///? }
import net.minecraft.client.player.AbstractClientPlayer;

public interface PoseOverwrite {

    public void updateState(AbstractClientPlayer entity, PlayerData data, PlayerModel playerModel);

}
