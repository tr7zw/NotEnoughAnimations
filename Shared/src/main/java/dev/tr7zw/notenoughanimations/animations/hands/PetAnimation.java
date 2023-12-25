package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class PetAnimation extends BasicAnimation {

    private Entity targetPet = null;
//    private double posDif = 0;

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.petAnimation;
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
        if (entHit != null && (entHit.getEntity().getType() == EntityType.WOLF
                || entHit.getEntity().getType() == EntityType.CAT)) {
            TamableAnimal pet = (TamableAnimal) entHit.getEntity();
            double dif = pet.getY() - entity.getY();
            if (Math.abs(dif) < 0.6) { // Making sure they are about on the same height
                targetPet = pet;
//                posDif = dif;
                return true;
            }
        }
        targetPet = null;
        return false;
    }

    private final BodyPart[] leftHanded = new BodyPart[] { BodyPart.LEFT_ARM };
    private final BodyPart[] rightHanded = new BodyPart[] { BodyPart.RIGHT_ARM };

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return entity.getMainArm() == HumanoidArm.RIGHT ? rightHanded : leftHanded;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 2100;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        if (Math.random() < 0.005)
            targetPet.handleEntityEvent((byte) 18);
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        AnimationUtil.applyArmTransforms(model, arm, -(Mth.lerp(-1f * (entity.getXRot() - 90f) / 180f, 1f, 2f)), -0.6f,
                0.3f + Mth.sin((System.currentTimeMillis() % 20000) / 60f) * 0.2f);
        targetPet = null;
    }

}
