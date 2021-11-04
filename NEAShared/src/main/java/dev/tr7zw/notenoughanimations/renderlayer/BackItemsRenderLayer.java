package dev.tr7zw.notenoughanimations.renderlayer;

import java.util.HashSet;
import java.util.Set;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.access.PlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BackItemsRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>{

    public BackItemsRenderLayer(
            RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderLayerParent) {
        super(renderLayerParent);
    }
    
    private boolean lazyInit = true;
    private static Set<Item> items = new HashSet<>();

    public static void update(Player player) {
        PlayerData data = (PlayerData) player;
        ItemStack[] backItems = data.getBackTools();
        if(backItems[0] == null) {
            backItems[0] = ItemStack.EMPTY;
            backItems[1] = ItemStack.EMPTY;
        }
        if(items.contains(player.getMainHandItem().getItem())) {
            backItems[0] = player.getMainHandItem();
        }
        if(items.contains(player.getOffhandItem().getItem())) {
            backItems[0] = player.getOffhandItem();
        }
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light,
            AbstractClientPlayer player, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4,
            float paramFloat5, float paramFloat6) {
        if(lazyInit) {
            lazyInit = false;
            init();
        }
        if(!NEAnimationsLoader.config.showLastUsedToolsOnBack) {
            return;
        }
        if(player.isInvisible() || player.isSleeping())return;
        if(!(player instanceof PlayerData)) {
            return;
        }
        PlayerData data = (PlayerData) player;
        ItemStack[] backItems = data.getBackTools();
        if (backItems[0] == null || backItems[0].isEmpty())
            return;
        if(player.getMainHandItem() == backItems[0] || player.getOffhandItem() == backItems[0]) {
            return;
        }
        renderBackItem(poseStack, multiBufferSource, light, player, backItems[0], false);
        renderBackItem(poseStack, multiBufferSource, light, player, backItems[0], true);
    }



    private void renderBackItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int light,
            AbstractClientPlayer player, ItemStack backItem, boolean flipped) {
        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        boolean lefthanded = (player.getMainArm() == HumanoidArm.LEFT);
        boolean wearingArmor = !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
        double offsetX = -0.0001;
        double offsetY = 0.5;
        double offsetZ = flipped ? (wearingArmor ? 0.21 : 0.181) : (wearingArmor ? 0.2 : 0.18);
        float rotationX = flipped ? 90 : -270f;
        float rotationY = flipped ? 55 : -60;
        float rotationZ = flipped ? 90 : 270f;
 
        poseStack.translate(offsetX, offsetY, offsetZ);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(rotationX));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(rotationY));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotationZ));
        
        Minecraft.getInstance().getItemInHandRenderer().renderItem(player, backItem, lefthanded ? ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND : ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, lefthanded,
                poseStack, multiBufferSource, light);
        poseStack.popPose();
    }
    
    
    
    private void init() {
        for(String itemKey : NEAnimationsLoader.config.backTools) {
            if(itemKey.contains(":")) {
                Item item = Registry.ITEM.get(new ResourceLocation(itemKey.split(":")[0], itemKey.split(":")[1]));
                if(item != Items.AIR) {
                    items.add(item);
                }
            }
        }
    }

}
