package dev.tr7zw.notenoughanimations.animations.fullbody;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.RenderStateHolder;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class CrawlingAnimation extends BasicAnimation {

    private BodyPart[] bodyParts = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM, BodyPart.LEFT_LEG,
            BodyPart.RIGHT_LEG };

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.enableCrawlingAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.getPose() == Pose.SWIMMING && !entity.isInWater();
    }

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return bodyParts;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 350;
    }

    private final float speedMul = 2.5F;
    private float swimAmount;
    private float attackTime;
    private float animationStep;
    private float animationStep2;
    private HumanoidArm humanoidArm;
    private float m;
    private float n;
    private float armMoveHight = 0.3707964F;
    private final float legPitch = 0.15F;
    private final float r = 0.33333334F;

    @Override
    protected void precalculate(AbstractClientPlayer entity, PlayerData data, PlayerModel model, float delta,
            float swing) {
        //? if >= 1.21.2 {

        RenderStateHolder.RenderStateData stateData = data.getData(RenderStateHolder.INSTANCE,
                RenderStateHolder.RenderStateData::new);
        swimAmount = stateData.renderState.swimAmount;
        attackTime = stateData.renderState.attackTime;
        //? } else {
        /*
         swimAmount = model.swimAmount;
         attackTime = model.attackTime;
        *///? }
        if (swimAmount > 0.0F) {
            animationStep = swing * speedMul % 26.0F;
            animationStep2 = animationStep + 13F;
            animationStep2 %= 26F;
            humanoidArm = getAttackArm(entity);
            m = (humanoidArm == HumanoidArm.RIGHT && attackTime > 0.0F) ? 0.0F : swimAmount;
            n = (humanoidArm == HumanoidArm.LEFT && attackTime > 0.0F) ? 0.0F : swimAmount;
        }
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        if (swimAmount > 0.0F) {
            if (part == BodyPart.RIGHT_ARM)
                if (animationStep < 14.0F) {
                    model.rightArm.xRot = Mth.lerp(m, model.rightArm.xRot, 0.0F);
                    model.rightArm.yRot = Mth.lerp(m, model.rightArm.yRot, 3.1415927F);
                    model.rightArm.zRot = Mth.lerp(m, model.rightArm.zRot,
                            3.1415927F - 1.8707964F * quadraticArmUpdate(animationStep) / quadraticArmUpdate(14.0F));
                } else if (animationStep >= 14.0F && animationStep < 24.0F) {
                    float o = (animationStep - 14.0F) / 10.0F;
                    model.rightArm.xRot = Mth.lerp(m, model.rightArm.xRot, -armMoveHight * o);
                    model.rightArm.yRot = Mth.lerp(m, model.rightArm.yRot, 3.1415927F);
                    model.rightArm.zRot = Mth.lerp(m, model.rightArm.zRot, 1.2707963F + 1.8707964F * o);
                } else if (animationStep >= 24.0F && animationStep < 26.0F) {
                    float p = (animationStep - 24.0F) / 2.0F;
                    model.rightArm.xRot = Mth.lerp(m, model.rightArm.xRot, -armMoveHight + armMoveHight * p);
                    model.rightArm.yRot = Mth.lerp(m, model.rightArm.yRot, 3.1415927F);
                    model.rightArm.zRot = Mth.lerp(m, model.rightArm.zRot, 3.1415927F);
                }
            if (part == BodyPart.LEFT_ARM)
                if (animationStep2 < 14.0F) {
                    model.leftArm.xRot = rotlerpRad(n, model.leftArm.xRot, 0.0F);
                    model.leftArm.yRot = rotlerpRad(n, model.leftArm.yRot, 3.1415927F);
                    model.leftArm.zRot = rotlerpRad(n, model.leftArm.zRot,
                            3.1415927F + 1.8707964F * quadraticArmUpdate(animationStep2) / quadraticArmUpdate(14.0F));
                } else if (animationStep2 >= 14.0F && animationStep2 < 24.0F) {
                    float o = (animationStep2 - 14.0F) / 10.0F;
                    model.leftArm.xRot = rotlerpRad(n, model.leftArm.xRot, -armMoveHight * o);
                    model.leftArm.yRot = rotlerpRad(n, model.leftArm.yRot, 3.1415927F);
                    model.leftArm.zRot = rotlerpRad(n, model.leftArm.zRot, 5.012389F - 1.8707964F * o);
                } else if (animationStep2 >= 24.0F && animationStep2 < 26.0F) {
                    float p = (animationStep2 - 24.0F) / 2.0F;
                    model.leftArm.xRot = rotlerpRad(n, model.leftArm.xRot, -armMoveHight + armMoveHight * p);
                    model.leftArm.yRot = rotlerpRad(n, model.leftArm.yRot, 3.1415927F);
                    model.leftArm.zRot = rotlerpRad(n, model.leftArm.zRot, 3.1415927F);
                }
        }
        tickCounter *= speedMul;
        if (part == BodyPart.LEFT_LEG) {
            model.leftLeg.xRot = Mth.lerp(swimAmount, model.leftLeg.xRot,
                    legPitch * Mth.cos(tickCounter * r + 3.1415927F));
            model.leftLeg.zRot = -0.1507964F;
        }
        if (part == BodyPart.RIGHT_LEG) {
            model.rightLeg.xRot = Mth.lerp(swimAmount, model.rightLeg.xRot, legPitch * Mth.cos(tickCounter * r));
            model.rightLeg.zRot = 0.1507964F;
        }
    }

    private float rotlerpRad(float f, float g, float h) {
        float i = (h - g) % 6.2831855F;
        if (i < -3.1415927F)
            i += 6.2831855F;
        if (i >= 3.1415927F)
            i -= 6.2831855F;
        return g + f * i;
    }

    private float quadraticArmUpdate(float f) {
        return -65.0F * f + f * f;
    }

    private HumanoidArm getAttackArm(Player livingEntity) {
        HumanoidArm humanoidArm = livingEntity.getMainArm();
        return (livingEntity.swingingArm == InteractionHand.MAIN_HAND) ? humanoidArm : humanoidArm.getOpposite();
    }

}
