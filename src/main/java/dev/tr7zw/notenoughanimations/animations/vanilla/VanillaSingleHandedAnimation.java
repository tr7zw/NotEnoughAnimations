package dev.tr7zw.notenoughanimations.animations.vanilla;

import java.util.EnumSet;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

public class VanillaSingleHandedAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return true;
    }

    private ArmPose rightArmPose;
    private ArmPose leftArmPose;
    // spotless:off
    //#if MC >= 11700
    private final EnumSet<ArmPose> singleHandedAnimatios = EnumSet.of(ArmPose.SPYGLASS, ArmPose.THROW_SPEAR);
    //#else
    //$$ private final EnumSet<ArmPose> singleHandedAnimatios = EnumSet.of(ArmPose.THROW_SPEAR);
    //#endif
    //spotless:on
    private final BodyPart[] left = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.BODY };
    private final BodyPart[] right = new BodyPart[] { BodyPart.RIGHT_ARM, BodyPart.BODY };

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        rightArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.LEFT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        leftArmPose = AnimationUtil.getArmPose(entity,
                entity.getMainArm() == HumanoidArm.RIGHT ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        return singleHandedAnimatios.contains(leftArmPose) || singleHandedAnimatios.contains(rightArmPose);
    }

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        if (singleHandedAnimatios.contains(leftArmPose)) {
            return left;
        }
        if (singleHandedAnimatios.contains(rightArmPose)) {
            return right;
        }
        // ???
        return new BodyPart[0];
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 3100;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        // Vanilla, do nothing
    }

}
