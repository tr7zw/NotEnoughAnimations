package dev.tr7zw.notenoughanimations.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.tr7zw.notenoughanimations.util.AnimationUtil;
import dev.tr7zw.notenoughanimations.util.MapRenderer;
import dev.tr7zw.notenoughanimations.util.NMSWrapper;
import dev.tr7zw.notenoughanimations.versionless.NEABaseMod;
import dev.tr7zw.util.NMSHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.HumanoidModel.ArmPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.EnchantTableRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
//#if MC >= 11700
import net.minecraft.client.model.geom.ModelLayers;
//#endif

public class HeldItemHandler {

    private Item filledMap = NMSHelper.getItem(NMSHelper.getResourceLocation("minecraft", "filled_map"));
    private Item book = NMSHelper.getItem(NMSHelper.getResourceLocation("minecraft", "book"));
    private Item writtenBook = NMSHelper.getItem(NMSHelper.getResourceLocation("minecraft", "written_book"));
    private Item writableBook = NMSHelper.getItem(NMSHelper.getResourceLocation("minecraft", "writable_book"));
    private Item enchantedBook = NMSHelper.getItem(NMSHelper.getResourceLocation("minecraft", "enchanted_book"));
    private Item knowledgeBook = NMSHelper.getItem(NMSHelper.getResourceLocation("minecraft", "knowledge_book"));
    public Set<Item> books = new HashSet<>(
            Arrays.asList(writableBook, writtenBook, enchantedBook, knowledgeBook, book));
    @SuppressWarnings("serial")
    public Map<Item, ResourceLocation> bookTextures = new HashMap<Item, ResourceLocation>() {
        {
            put(knowledgeBook, NMSHelper.getResourceLocation("notenoughanimations", "textures/recipe_book.png"));
        }
    };
    public Set<Item> glintingBooks = new HashSet<>(Arrays.asList(enchantedBook));
    private BookModel bookModel = null;

    public void onRenderItem(LivingEntity entity, EntityModel<?> model, ItemStack itemStack, HumanoidArm arm,
            PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
        if (bookModel == null) {
            //#if MC >= 11700
            bookModel = new BookModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.BOOK));
            //#else
            //$$ bookModel = new BookModel();
            //#endif
        }
        if (entity.isSleeping()) { // Stop holding stuff in your sleep
            if (NEABaseMod.config.dontHoldItemsInBed) {
                info.cancel();
            }
            return;
        }
        if (NMSWrapper.hasCustomModel(itemStack)) {
            // Don't replace the model of items with a custom model
            return;
        }
        if (model instanceof ArmedModel armedModel && model instanceof HumanoidModel<?> humanoid) {
            if ((arm == HumanoidArm.RIGHT && humanoid.rightArm.visible)
                    || (arm == HumanoidArm.LEFT && humanoid.leftArm.visible)) {
                if (NEABaseMod.config.enableInWorldMapRendering) {
                    if (arm == entity.getMainArm() && entity.getMainHandItem().getItem().equals(filledMap)) { // Mainhand
                                                                                                              // with
                                                                                                              // or
                                                                                                              // without
                                                                                                              // the
                                                                                                              // offhand
                        matrices.pushPose();
                        armedModel.translateToHand(arm, matrices);
                        matrices.mulPose(NMSHelper.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(NMSHelper.YP.rotationDegrees(200.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((bl ? -1 : 1) / 16.0f,
                                0.125 + (entity.getOffhandItem().isEmpty() ? 0.15 : 0), -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack,
                                !entity.getOffhandItem().isEmpty(), entity.getMainArm() == HumanoidArm.LEFT);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                    if (arm != entity.getMainArm() && entity.getOffhandItem().getItem().equals(filledMap)) { // Only
                                                                                                             // offhand
                        matrices.pushPose();
                        armedModel.translateToHand(arm, matrices);
                        matrices.mulPose(NMSHelper.XP.rotationDegrees(-90.0f));
                        matrices.mulPose(NMSHelper.YP.rotationDegrees(200.0f));
                        boolean bl = arm == HumanoidArm.LEFT;
                        matrices.translate((bl ? -1 : 1) / 16.0f, 0.125, -0.625);
                        MapRenderer.renderFirstPersonMap(matrices, vertexConsumers, light, itemStack, true, false);
                        matrices.popPose();
                        info.cancel();
                        return;
                    }
                }
                if (NEABaseMod.config.enableInWorldBookRendering) {
                    Item item = entity.getMainHandItem().getItem();
                    if (arm == entity.getMainArm() && books.contains(item)) {
                        renderBook(entity, 0, itemStack, arm, matrices, vertexConsumers, light, armedModel,
                                glintingBooks.contains(item), item);
                        info.cancel();
                        return;
                    }
                    item = entity.getOffhandItem().getItem();
                    if (arm != entity.getMainArm() && books.contains(item)) {
                        renderBook(entity, 0, itemStack, arm, matrices, vertexConsumers, light, armedModel,
                                glintingBooks.contains(item), item);
                        info.cancel();
                        return;
                    }
                }
            }
        }

        if (NEABaseMod.config.enableOffhandHiding && entity instanceof AbstractClientPlayer player) {
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

    private void renderBook(LivingEntity entity, float delta, ItemStack itemStack, HumanoidArm arm, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, ArmedModel armedModel, boolean glow, Item item) {
        matrices.pushPose();
        armedModel.translateToHand(arm, matrices);

        matrices.mulPose(NMSHelper.YP.rotationDegrees(100));
        matrices.mulPose(NMSHelper.ZP.rotationDegrees(-100));
        matrices.translate(-0.56, 0.34, 0);// arm == HumanoidArm.RIGHT ? 0 : 0.09);

        float g = entity.tickCount + delta;
        float l = 0;// Mth.lerp(delta, enchantmentTableBlockEntity.oFlip,
                    // enchantmentTableBlockEntity.flip);
        float m = Mth.frac(l + 0.25F) * 1.6F - 0.3F;
        float n = Mth.frac(l + 0.75F) * 1.6F - 0.3F;
        float o = 1;// Mth.lerp(delta, enchantmentTableBlockEntity.oOpen,
                    // enchantmentTableBlockEntity.open);
        this.bookModel.setupAnim(g, Mth.clamp(m, 0.0F, 1.0F), Mth.clamp(n, 0.0F, 1.0F), o);
        VertexConsumer vertexConsumer;
        if (bookTextures.containsKey(item)) {
            vertexConsumer = ItemRenderer.getFoilBuffer(vertexConsumers, RenderType.entitySolid(bookTextures.get(item)),
                    true, glow);
        } else {
            //#if MC >= 12102
            vertexConsumer = EnchantTableRenderer.BOOK_LOCATION.buffer(vertexConsumers, RenderType::entitySolid, true,
                    glow);
            //#else
            //$$vertexConsumer = EnchantTableRenderer.BOOK_LOCATION.buffer(vertexConsumers, RenderType::entitySolid, glow);
            //#endif
        }
        //#if MC >= 12100
        bookModel.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, Integer.MAX_VALUE);
        //#else
        //$$ bookModel.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //#endif
        matrices.popPose();
        if (item == writtenBook) {
            matrices.pushPose();
            armedModel.translateToHand(arm, matrices);
            renderText(entity, matrices, itemStack, armedModel, arm);
            matrices.popPose();
        }

    }

    // Broken mess, fix me
    @SuppressWarnings("resource")
    private void renderText(LivingEntity entity, PoseStack matrices, ItemStack itemStack, ArmedModel armedModel,
            HumanoidArm arm) {
        //        BookAccess bookAccess = fromItem(itemStack);
        //        FormattedText formattedText = bookAccess.getPage(0);
        //        matrices.scale(-0.0025f, 0.0025f, -0.0025f);
        //        
        //        matrices.mulPose(Axis.XP.rotationDegrees(-90)); // tilt back
        //        matrices.mulPose(Axis.YP.rotationDegrees(29)); // tilt left right
        //        matrices.mulPose(Axis.ZP.rotationDegrees(14)); // rotation
        //        //matrices.translate(entity.getX()%1*1000, -entity.getY()%1*1000, -entity.getZ()%1*1000);
        //        matrices.translate(-120, -230, 130);
        //        // y is hoch runter
        //        
        //        //System.out.println(entity.getX()%1*1000 + " " + entity.getY()%1*1000 + " " + entity.getZ()%1*1000);
        //        List<FormattedCharSequence> text = Minecraft.getInstance().font.split(formattedText, 114);
        //        int n = Math.min(128 / 9, text.size());
        //        for (int o = 0; o < n; o++) {
        //            FormattedCharSequence formattedCharSequence = text.get(o);
        //            Minecraft.getInstance().font.draw(matrices, formattedCharSequence, (36), (32 + o * 9), 0);
        //        }
    }

    //    private BookAccess fromItem(ItemStack itemStack) {
    //        // spotless:off
//    	//#if MC >= 11700
//        //$$ if (itemStack.is(Items.WRITTEN_BOOK))
//        //$$    return new BookViewScreen.WrittenBookAccess(itemStack);
//        //$$ if (itemStack.is(Items.WRITABLE_BOOK))
//        //$$    return new BookViewScreen.WritableBookAccess(itemStack);
//	//$$	//#else
//        //$$ if (itemStack.getItem() == (Items.WRITTEN_BOOK))
//        //$$     return new BookViewScreen.WrittenBookAccess(itemStack);
//        //$$ if (itemStack.getItem() == (Items.WRITABLE_BOOK))
//        //$$     return new BookViewScreen.WritableBookAccess(itemStack);
//		//#endif
//		//spotless:on
    //        return BookViewScreen.EMPTY_ACCESS;
    //    }

}
