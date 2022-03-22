package dev.tr7zw.notenoughanimations.animations.fullbody;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public class RotationFixerAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.keepBodyRotatedWithHead;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity == Minecraft.getInstance().player;
    }

    private final BodyPart[] parts = new BodyPart[] {BodyPart.BODY};
    
    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return parts;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 100;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        data.disableBodyRotation(true);
        entity.setYBodyRot(entity.getYHeadRot());
        entity.yBodyRotO = entity.yHeadRotO;
    }

}
