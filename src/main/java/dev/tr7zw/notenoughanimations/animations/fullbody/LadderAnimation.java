package dev.tr7zw.notenoughanimations.animations.fullbody;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.api.BasicAnimation;
import dev.tr7zw.notenoughanimations.api.PoseOverwrite;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.util.RenderStateHolder;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.notenoughanimations.versionless.animations.BodyPart;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LadderAnimation extends BasicAnimation implements PoseOverwrite {

    @Override
    public boolean isEnabled() {
        return NEABaseMod.config.enableLadderAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        if (entity.onClimbable() && !entity.onGround() && entity.getLastClimbablePos().isPresent()) {
            for (Class<? extends Block> blocktype : ladderLikeBlocks) {
                if (blocktype.isAssignableFrom(
                        // spotless:off 
                        //#if MC >= 11800
                        entity.level().getBlockState(entity.getLastClimbablePos().get()).getBlock().getClass()))
                    //#else
                    //$$ entity.level.getBlockState(entity.getLastClimbablePos().get()).getBlock().getClass()))
                    //#endif
                    //spotless:on

                    return true;
            }
            return false;
        }
        return false;
    }

    private final Set<Class<? extends Block>> ladderLikeBlocks = new HashSet<>(
            Arrays.asList(LadderBlock.class, TrapDoorBlock.class));

    private final BodyPart[] parts = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM, BodyPart.BODY,
            BodyPart.LEFT_LEG, BodyPart.RIGHT_LEG, BodyPart.HEAD };
    private final BodyPart[] partsSneakingRight = new BodyPart[] { BodyPart.RIGHT_ARM, BodyPart.BODY, BodyPart.LEFT_LEG,
            BodyPart.RIGHT_LEG, BodyPart.HEAD };
    private final BodyPart[] partsSneakingLeft = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.BODY, BodyPart.LEFT_LEG,
            BodyPart.RIGHT_LEG, BodyPart.HEAD };

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        if (entity.isCrouching() && entity.getDeltaMovement().y == -0.0784000015258789) { // magic value while being not
                                                                                          // moving on a ladder cause mc
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                return partsSneakingLeft;
            } else {
                return partsSneakingRight;
            }
        }
        return parts;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 1400;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel model, BodyPart part, float delta,
            float tickCounter) {
        if (part == BodyPart.HEAD) {
            // this gets handled in the body block
            return;
        }
        if (part == BodyPart.BODY) {
            if (NEABaseMod.config.enableRotateToLadder) {
                // spotless:off 
                //#if MC >= 12005
                BlockState blockState = entity.getInBlockState();
                //#else
                //$$ BlockState blockState = entity.getFeetBlockState();
                //#endif
                //spotless:on
                if (blockState.hasProperty(HorizontalDirectionalBlock.FACING)) {
                    Direction dir = blockState.getValue(HorizontalDirectionalBlock.FACING);
                    data.setDisableBodyRotation(true);
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
                    AnimationUtil.minMaxHeadRotation(entity, model);
                }
                return;
            }
        }

        if (part == BodyPart.LEFT_LEG || part == BodyPart.RIGHT_LEG) {
            float rotation = -Mth.cos((float) (entity.getY() * NEABaseMod.config.ladderAnimationArmSpeed));
            rotation *= NEABaseMod.config.ladderAnimationAmplifier;
            if (part == BodyPart.LEFT_LEG) {
                rotation *= -1;
            }
            AnimationUtil.applyTransforms(model, part, -1 - rotation, -0.2f, 0.3f);
            return;
        }
        float rotation = -Mth.cos((float) (entity.getY() * NEABaseMod.config.ladderAnimationArmSpeed));
        rotation *= NEABaseMod.config.ladderAnimationAmplifier;
        // arms
        if (part == BodyPart.LEFT_ARM)
            rotation *= -1;
        AnimationUtil.applyTransforms(model, part, -NEABaseMod.config.ladderAnimationArmHeight - rotation, -0.2f, 0.3f);
    }

    @Override
    public void updateState(AbstractClientPlayer entity, PlayerData data, PlayerModel playerModel) {
        if (entity.isCrouching() && isValid(entity, data)) {
            data.setPoseOverwrite(entity.getPose());
            entity.setPose(Pose.STANDING);
            //#if MC >= 12102
            RenderStateHolder.RenderStateData stateData = data.getData(RenderStateHolder.INSTANCE,
                    RenderStateHolder.RenderStateData::new);
            stateData.renderState.isCrouching = false;
            //#else
            //$$playerModel.crouching = false;
            //#endif
        }
    }

}
