package dev.tr7zw.notenoughanimations.animations.hands;

import java.util.HashSet;
import java.util.Set;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import dev.tr7zw.notenoughanimations.animations.BasicAnimation;
import dev.tr7zw.notenoughanimations.animations.BodyPart;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MapHoldingAnimation extends BasicAnimation {

    private Set<Item> compatibleMaps = new HashSet<>();
    
    @Override
    public boolean isEnabled() {
        bind();
        return NEAnimationsLoader.config.enableInWorldMapRendering || !compatibleMaps.isEmpty();
    }
    
    private void bind() {
        compatibleMaps.clear();
        Item invalid = BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft", "air"));
        compatibleMaps.add(BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft", "filled_map")));
        Item antiqueAtlas = BuiltInRegistries.ITEM.get(new ResourceLocation("antiqueatlas", "antique_atlas"));
        if (invalid != antiqueAtlas) {
            compatibleMaps.add(antiqueAtlas);
            NEAnimationsLoader.LOGGER.info("Added AntiqueAtlas support to Not Enough Animations!");
        }
    }

    @Override
    public boolean isValid(AbstractClientPlayer entity, PlayerData data) {
        ItemStack itemInMainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack itemInOffHand = entity.getItemInHand(InteractionHand.OFF_HAND);
        if(compatibleMaps.contains(itemInMainHand.getItem()) && itemInOffHand.isEmpty()) {
            target = bothHands;
            return true;
        }
        if(compatibleMaps.contains(itemInMainHand.getItem()) && !itemInOffHand.isEmpty()) {
            target = entity.getMainArm() == HumanoidArm.RIGHT ? right : left;
            return true;
        }
        if(compatibleMaps.contains(itemInOffHand.getItem()) && !itemInOffHand.isEmpty()) {
            target = entity.getMainArm() == HumanoidArm.RIGHT ? left : right;
            return true;
        }

        return false;
    }

    private final BodyPart[] bothHands = new BodyPart[] {BodyPart.LEFT_ARM, BodyPart.RIGHT_ARM};
    private final BodyPart[] left = new BodyPart[] {BodyPart.LEFT_ARM};
    private final BodyPart[] right = new BodyPart[] {BodyPart.RIGHT_ARM};
    private BodyPart[] target = bothHands;
    
    @Override
    public BodyPart[] getBodyParts(AbstractClientPlayer entity, PlayerData data) {
        return target;
    }

    @Override
    public int getPriority(AbstractClientPlayer entity, PlayerData data) {
        return 300;
    }

    @Override
    public void apply(AbstractClientPlayer entity, PlayerData data, PlayerModel<AbstractClientPlayer> model,
            BodyPart part, float delta, float tickCounter) {
        HumanoidArm arm = part == BodyPart.LEFT_ARM ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
        if(target == bothHands) {
            AnimationUtil.applyArmTransforms(model, arm, -(Mth.lerp(-1f * (entity.getXRot() - 90f) / 180f, 0.5f, 1.5f)),
                    -0.4f, 0.3f);
            return;
        }
        AnimationUtil.applyArmTransforms(model, arm, -(Mth.lerp(-1f * (entity.getXRot() - 90f) / 180f, 0.5f, 1.5f)), 0f,
                0.3f);
    }

}
