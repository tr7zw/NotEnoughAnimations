package dev.tr7zw.notenoughanimations.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import dev.tr7zw.notenoughanimations.NEAnimationsLoader;
import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.util.MapRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen.BookAccess;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HeldItemHandler {

    private Item filledMap = Registry.ITEM.get(new ResourceLocation("minecraft", "filled_map"));
    private Item book = Registry.ITEM.get(new ResourceLocation("minecraft", "book"));
    private Item writtenBook = Registry.ITEM.get(new ResourceLocation("minecraft", "written_book"));
    private Item writableBook = Registry.ITEM.get(new ResourceLocation("minecraft", "writable_book"));
    private Item enchantedBook = Registry.ITEM.get(new ResourceLocation("minecraft", "enchanted_book"));
    private Item knowledgeBook = Registry.ITEM.get(new ResourceLocation("minecraft", "knowledge_book"));
    public Set<Item> books = new HashSet<>(Arrays.asList(writableBook, writtenBook, enchantedBook, knowledgeBook, book));
    @SuppressWarnings("serial")
    public Map<Item, ResourceLocation> bookTextures = new HashMap<>() {
        {
            put(knowledgeBook,  new ResourceLocation("notenoughanimations", "textures/recipe_book.png"));
        }
    };
    public Set<Item> glintingBooks = new HashSet<>(Arrays.asList(enchantedBook));
    private BookModel bookModel = null;

    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
        if(bookModel == null) {
            bookModel = new BookModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BOOK));
        }
        if (entity.isSleeping()) { // Stop holding stuff in your sleep
            if(NEAnimationsLoader.config.dontHoldItemsInBed) {
                info.cancel();
            }
            return;
        }
        if (model instanceof ArmedModel && model instanceof HumanoidModel) {
            ArmedModel armedModel = (ArmedModel) model;
            HumanoidModel<?> humanoid = (HumanoidModel<?>) model;
            if((arm == HumanoidArm.RIGHT && humanoid.rightArm.visible) || (arm == HumanoidArm.LEFT && humanoid.leftArm.visible)) {
                if(NEAnimationsLoader.config.enableInWorldMapRendering) {
                    if (arm == entity.getMainArm() && entity.getMainHandItem().getItem().equals(filledMap)) { // Mainhand with
                                                                                                              // or without the
                                                                                                              // offhand
                        matrices.pushPose();
                        armedModel.translateToHand(arm, matrices);
                        matrices.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(Vector3f.YP.rotationDegrees(200.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125 + (entity.getOffhandItem().isEmpty() ? 0.15 : 0), -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack,
                                !entity.getOffhandItem().isEmpty(), entity.getMainArm() == HumanoidArm.LEFT);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                    if (arm != entity.getMainArm() && entity.getOffhandItem().getItem().equals(filledMap)) { // Only offhand
                        matrices.pushPose();
                        armedModel.translateToHand(arm, matrices);
                        matrices.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(Vector3f.YP.rotationDegrees(200.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((double) ((float) (bl ? -1 : 1) / 16.0f), 0.125, -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack, true, false);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                }
                if(NEAnimationsLoader.config.enableInWorldBookRendering) {
                    Item item = entity.getMainHandItem().getItem();
                    if(arm == entity.getMainArm() && books.contains(item)) {
                        renderBook(entity, 0, itemStack, arm, matrices, vertexConsumers, light, armedModel, glintingBooks.contains(item), item);
                        info.cancel();
                        return;
                    }
                    item = entity.getOffhandItem().getItem();
                    if(arm != entity.getMainArm() && books.contains(item)) {
                        renderBook(entity, 0, itemStack, arm, matrices, vertexConsumers, light, armedModel, glintingBooks.contains(item), item);
                        info.cancel();
                        return;
                    }
                }
            }
        }

        if (NEAnimationsLoader.config.enableOffhandHiding && entity instanceof AbstractClientPlayer) {
            AbstractClientPlayer player = (AbstractClientPlayer) entity;
            ArmPose armPose = AnimationUtil.getArmPose(player, InteractionHand.MAIN_HAND);
            ArmPose armPose2 = AnimationUtil.getArmPose(player, InteractionHand.OFF_HAND);
            if (!(AnimationUtil.isUsingboothHands(armPose) || AnimationUtil.isUsingboothHands(armPose2)))
                return;
            if (armPose.isTwoHanded()) {
                armPose2 = player.getOffhandItem().isEmpty() ? ArmPose.EMPTY : ArmPose.ITEM;
            }

            if (player.getMainArm() == HumanoidArm.RIGHT) {
                if (arm == HumanoidArm.RIGHT && AnimationUtil.isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.LEFT && AnimationUtil.isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            } else {
                if (arm == HumanoidArm.LEFT && AnimationUtil.isUsingboothHands(armPose2)) {
                    info.cancel();
                    return;
                } else if (arm == HumanoidArm.RIGHT && AnimationUtil.isUsingboothHands(armPose)) {
                    info.cancel();
                    return;
                }
            }
        }
    }

    private void renderBook(LivingEntity entity, float delta, ItemStack itemStack, HumanoidArm arm, PoseStack matrices, MultiBufferSource vertexConsumers,
            int light, ArmedModel armedModel, boolean glow, Item item) {
        matrices.pushPose();
        armedModel.translateToHand(arm, matrices);
        
        matrices.mulPose(Vector3f.YP.rotationDegrees(100));
        matrices.mulPose(Vector3f.ZP.rotationDegrees(-100));
        matrices.translate(-0.56,0.34, 0);//arm == HumanoidArm.RIGHT ? 0 : 0.09);

        float g = entity.tickCount + delta;
        float l = 0;//Mth.lerp(delta, enchantmentTableBlockEntity.oFlip, enchantmentTableBlockEntity.flip);
        float m = Mth.frac(l + 0.25F) * 1.6F - 0.3F;
        float n = Mth.frac(l + 0.75F) * 1.6F - 0.3F;
        float o = 1;//Mth.lerp(delta, enchantmentTableBlockEntity.oOpen, enchantmentTableBlockEntity.open);
        this.bookModel.setupAnim(g, Mth.clamp(m, 0.0F, 1.0F), Mth.clamp(n, 0.0F, 1.0F), o);
        VertexConsumer vertexConsumer;
        if(bookTextures.containsKey(item)) {
            vertexConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumers, RenderType.entitySolid(bookTextures.get(item)), true, glow);
        }else {
            vertexConsumer= EnchantTableRenderer.BOOK_LOCATION.buffer(vertexConsumers,RenderType::entitySolid, glow);
        }
        
        bookModel.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.popPose();
        if(item == writtenBook) {
            matrices.pushPose();
            armedModel.translateToHand(arm, matrices);
            renderText(entity, matrices, itemStack, armedModel, arm);
            matrices.popPose();
        }
        
    }
    
    // Broken mess, fix me
    @SuppressWarnings("resource")
    private void renderText(LivingEntity entity, PoseStack matrices, ItemStack itemStack, ArmedModel armedModel, HumanoidArm arm) {
        BookAccess bookAccess = fromItem(itemStack);
        FormattedText formattedText = bookAccess.getPage(0);
        matrices.scale(-0.0025f, 0.0025f, -0.0025f);
        
        matrices.mulPose(Vector3f.XP.rotationDegrees(-90)); // tilt back
        matrices.mulPose(Vector3f.YP.rotationDegrees(29)); // tilt left right
        matrices.mulPose(Vector3f.ZP.rotationDegrees(14)); // rotation
        //matrices.translate(entity.getX()%1*1000, -entity.getY()%1*1000, -entity.getZ()%1*1000);
        matrices.translate(-120, -230, 130);
        // y is hoch runter
        
        //System.out.println(entity.getX()%1*1000 + " " + entity.getY()%1*1000 + " " + entity.getZ()%1*1000);
        List<FormattedCharSequence> text = Minecraft.getInstance().font.split(formattedText, 114);
        int n = Math.min(128 / 9, text.size());
        for (int o = 0; o < n; o++) {
            FormattedCharSequence formattedCharSequence = text.get(o);
            Minecraft.getInstance().font.draw(matrices, formattedCharSequence, (36), (32 + o * 9), 0);
        }
    }
    
    private BookAccess fromItem(ItemStack itemStack) {
        if (itemStack.is(Items.WRITTEN_BOOK))
            return new BookViewScreen.WrittenBookAccess(itemStack);
        if (itemStack.is(Items.WRITABLE_BOOK))
            return new BookViewScreen.WritableBookAccess(itemStack);
        return BookViewScreen.EMPTY_ACCESS;
    }

}
