package dev.tr7zw.notenoughanimations.api;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public interface PoseOverwrite {

    public void updateState(AbstractClientPlayer entity, PlayerData data, PlayerModel playerModel);

}
