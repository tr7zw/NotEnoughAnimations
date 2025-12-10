package dev.tr7zw.notenoughanimations.animations.fullbody;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.level.block.Blocks;

public class BurningAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.burningAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.isOnFire() && !entity.hasEffect(MobEffects.FIRE_RESISTANCE);
    }

    private BodyPart[] parts = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM, BodyPart.HEAD };

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return parts;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 400;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        if (part == BodyPart.HEAD) {
            AnimationUtil.setHeadYRot(model, model.head.yRot + Mth.sin(entity.tickCount) * 0.1f);
            return;
        }
        float armHeight = Mth.sin(entity.tickCount) * 0.1f;
        if (part == BodyPart.LEFT_ARM)
            armHeight *= -1;
        AnimationUtil.applyArmTransforms(model, part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT,
                -2.6f + armHeight, -0.2f, -0.3f);
    }

}
