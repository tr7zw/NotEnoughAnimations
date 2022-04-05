package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class NarutoRunningAnimation extends BasicAnimation {
    
    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.narutoRunning;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.isSprinting();
    }

    private final BodyPart[] arms = new BodyPart[] {BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM};
    
    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return arms;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 500;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        if(part == BodyPart.LEFT_ARM) {
            AnimationUtil.applyArmTransforms(model, HumanoidArm.LEFT, 1f, -0.2f,
                    0.3f);
        }
        if(part == BodyPart.RIGHT_ARM) {
            AnimationUtil.applyArmTransforms(model, HumanoidArm.RIGHT, 1f, -0.2f,
                    0.3f);
        }
    }

}
