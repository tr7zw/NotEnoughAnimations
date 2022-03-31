package dev.tr7zw.notenoughanimations.animations.hands;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class ItemSwapAnimation extends BasicAnimation {

    @Override
    public boolean isEnabled() {
        return NEAnimationsLoader.config.itemSwapAnimation;
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        if (data.getLastAnimationSwapTick() != entity.tickCount) { // run data updates
            data.setLastAnimationSwapTick(entity.tickCount);
            if (data.getLastHeldItems()[0] == null) { // init vars
                data.getLastHeldItems()[0] = entity.getMainHandItem();
                data.getLastHeldItems()[1] = entity.getOffhandItem();
            }
            ItemStack mainHand = entity.getMainHandItem();
            ItemStack offHand = entity.getOffhandItem();
            if ((!mainHand.isEmpty() || !offHand.isEmpty())
                    && data.getLastHeldItems()[0].getItem() != data.getLastHeldItems()[1].getItem()
                    && data.getLastHeldItems()[0].getItem() == offHand.getItem()
                    && data.getLastHeldItems()[1].getItem() == mainHand.getItem()) {
                data.setItemSwapAnimationTimer(10);
            }
            data.getLastHeldItems()[0] = entity.getMainHandItem();
            data.getLastHeldItems()[1] = entity.getOffhandItem();
            if (data.getItemSwapAnimationTimer() > 0) {
                data.setItemSwapAnimationTimer(data.getItemSwapAnimationTimer() - 1);
            }
        }
        return data.getItemSwapAnimationTimer() > 0;
    }

    private final BodyPart[] parts = new BodyPart[] { BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM };

    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return parts;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 3500;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {

        int animationTick = data.getItemSwapAnimationTimer();
        float position = animationTick / 10f * -1f;
        position = Mth.lerp(delta, (animationTick + 1) / 10f * -1f, position);
        if (part == BodyPart.LEFT_ARM)
            AnimationUtil.applyArmTransforms(model, HumanoidArm.LEFT, -0.5f, 0.2f, position);
        if (part == BodyPart.RIGHT_ARM)
            AnimationUtil.applyArmTransforms(model, HumanoidArm.RIGHT, -0.5f, 0.2f, position);

    }

}
