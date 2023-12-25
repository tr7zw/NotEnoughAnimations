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

public class HugAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.huggingAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        if (!entity.isCrouching()) {
            return false;
        }
        double d = 1;// range
        Vec3 vec3 = entity.getEyePosition(0);
        Vec3 vec32 = entity.getViewVector(1.0F);
        Vec3 vec33 = vec3.add(vec32.x * d, vec32.y * d, vec32.z * d);
        AABB aABB = entity.getBoundingBox().expandTowards(vec32.scale(d)).inflate(1.0D, 1.0D, 1.0D);
        EntityHitResult entHit = ProjectileUtil.getEntityHitResult(entity, vec3, vec33, aABB, en -> (!en.isSpectator()),
                d);
        if (entHit != null && (entHit.getEntity().getType() == EntityType.PLAYER)) {
            AbstractClientPlayer otherPlayer = (AbstractClientPlayer) entHit.getEntity();
            double dif = otherPlayer.getY() - entity.getY();
            if (otherPlayer.isCrouching() && Math.abs(dif) < 0.3) { // Making sure they are about on the same height
                return true;
            }
        }
        return false;
    }

    private final BodyPart[] arms = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM };

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return arms;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 2100;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        if (part == BodyPart.LEFT_ARM) {
            AnimationUtil.applyArmTransforms(model, HumanoidArm.LEFT, -1f, -0.2f, 0.3f);
        }
        if (part == BodyPart.RIGHT_ARM) {
            AnimationUtil.applyArmTransforms(model, HumanoidArm.RIGHT, -1.5f, -0.2f, 0.3f);
        }
    }

}
