package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;

public class SleepAnimation extends BasicAnimation{

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.freezeArmsInBed;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.isSleeping();
    }

    private final BodyPart[] bothHands = new BodyPart[] {BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM};
    
    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return bothHands;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 100;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        AnimationUtil.applyArmTransforms(model, arm, 0, 0f, 0f);
    }

}
