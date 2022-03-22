package dev.tr7zw.notenoughanimations.animations.fullbody;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ClimbingAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.enableLadderAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        return entity.onClimbable() && !entity.isOnGround();
    }

    private final BodyPart[] parts = new BodyPart[] {BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM, BodyPart.BODY};
    
    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return parts; // legs later too?
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 1600;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        if(part == BodyPart.BODY && NEAnimationsLoader.config.enableRotateToLadder) {
            BlockState blockState = entity.getFeetBlockState();
            if (blockState.hasProperty(HorizontalDirectionalBlock.FACING)) {
                Direction dir = blockState.getValue(HorizontalDirectionalBlock.FACING);
                data.disableBodyRotation(true);
                switch (dir) {
                case NORTH:
                    entity.setYBodyRot(0);
                    entity.yBodyRotO = 0;
                    break;
                case EAST:
                    entity.setYBodyRot(90);
                    entity.yBodyRotO = 90;
                    break;
                case SOUTH:
                    entity.setYBodyRot(180);
                    entity.yBodyRotO = 180;
                    break;
                case WEST:
                    entity.setYBodyRot(270);
                    entity.yBodyRotO = 270;
                    break;
                default:
                }
                minMaxHeadRotation(entity, model);
            }
            return;
        }
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        float rotation = -Mth.cos((float) (entity.getY() * NEAnimationsLoader.config.ladderAnimationArmSpeed));
        rotation *= NEAnimationsLoader.config.ladderAnimationAmplifier;
        if (arm == HumanoidArm.LEFT)
            rotation *= -1;
        AnimationUtil.applyArmTransforms(model, arm, -NEAnimationsLoader.config.ladderAnimationArmHeight - rotation, -0.2f, 0.3f);
    }
    
    private void minMaxHeadRotation(Player livingEntity, PlayerModel<AbstractClientPlayer> model) {
        float value = wrapDegrees(model.head.yRot);
        float min = wrapDegrees(model.body.yRot - Mth.HALF_PI);
        float max = wrapDegrees(model.body.yRot + Mth.HALF_PI);
        value = Math.min(value, max);
        value = Math.max(value, min);
        model.head.yRot = value;
        model.hat.yRot = value;
    }
    
    private float wrapDegrees(float f) {
        float g = f % 6.28318512f;
        if (g >= 3.14159256f) {
            g -= 6.28318512f;
        }
        if (g < -3.14159256f) {
            g += 6.28318512f;
        }
        return g;
    }

}
