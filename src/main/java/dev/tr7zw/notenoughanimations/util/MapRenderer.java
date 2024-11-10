package dev.tr7zw.notenoughanimations.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.tr7zw.util.NMSHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

//spotless:off
//#if MC >= 11903
import org.joml.Matrix4f;
//#else
//$$ import com.mojang.math.Matrix4f;
//#endif
//spotless:on

public class MapRenderer {

    private static final RenderType MAP_BACKGROUND = RenderType
            .text(NMSHelper.getResourceLocation("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType
            .text(NMSHelper.getResourceLocation("textures/map/map_background_checkerboard.png"));

    public static void renderFirstPersonMap(PoseStack matrices, MultiBufferSource vertexConsumers, int light,
            ItemStack stack, boolean small, boolean lefthanded) {
        Minecraft client = Minecraft.getInstance();

        if (small) {
            matrices.mulPose(NMSHelper.YP.rotationDegrees(160.0f));
            matrices.mulPose(NMSHelper.ZP.rotationDegrees(180.0f));
            matrices.scale(0.38f, 0.38f, 0.38f);

            matrices.translate(-0.1, -1.2, 0.0);
            matrices.scale(0.0098125f, 0.0098125f, 0.0098125f);
        } else {
            if (lefthanded) {
                matrices.mulPose(NMSHelper.YP.rotationDegrees(160.0f));
                matrices.mulPose(NMSHelper.ZP.rotationDegrees(150.0f));
                matrices.scale(0.38f, 0.38f, 0.38f);

                matrices.translate(+0.5, -1.3, 0.0);
            } else {
                matrices.mulPose(NMSHelper.YP.rotationDegrees(160.0f));
                matrices.mulPose(NMSHelper.ZP.rotationDegrees(210.0f));
                matrices.scale(0.38f, 0.38f, 0.38f);

                matrices.translate(-1.0, -1.8, 0.0);
            }

            matrices.scale(0.0138125f, 0.0138125f, 0.0138125f);
        }
        // spotless:off
      //#if MC <= 12004
      //$$  Integer mapid = MapItem.getMapId(stack);
      //#else
        net.minecraft.world.level.saveddata.maps.MapId mapid = stack.get(net.minecraft.core.component.DataComponents.MAP_ID);
      //#endif
      //spotless:on
        MapItemSavedData mapState = MapItem.getSavedData(stack, client.level);
        com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer = vertexConsumers
                .getBuffer(mapState == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
        Matrix4f matrix4f = matrices.last().pose();
        addVertex(vertexConsumer, matrix4f, -7.0f, 135.0f, 0.0f, 0, 1, light);
        addVertex(vertexConsumer, matrix4f, 135.0f, 135.0f, 0.0f, 1, 1, light);
        addVertex(vertexConsumer, matrix4f, 135.0f, -7.0f, 0.0f, 1, 0, light);
        addVertex(vertexConsumer, matrix4f, -7.0f, -7.0f, 0.0f, 0, 0, light);
        // mirrored back site
        vertexConsumer = vertexConsumers.getBuffer(MAP_BACKGROUND);
        addVertex(vertexConsumer, matrix4f, -7.0f, -7.0f, 0.0f, 0, 0, light);
        addVertex(vertexConsumer, matrix4f, 135.0f, -7.0f, 0.0f, 1, 0, light);
        addVertex(vertexConsumer, matrix4f, 135.0f, 135.0f, 0.0f, 1, 1, light);
        addVertex(vertexConsumer, matrix4f, -7.0f, 135.0f, 0.0f, 0, 1, light);

        if (mapState != null) {
            // spotless:off
            //#if MC >= 12102
            net.minecraft.client.renderer.state.MapRenderState mapRenderState = new net.minecraft.client.renderer.state.MapRenderState();
            client.getMapRenderer().extractRenderState(mapid, mapState, mapRenderState);
            client.getMapRenderer().render(mapRenderState, matrices, vertexConsumers, false, light);
            //#elseif MC >= 11700
            //$$client.gameRenderer.getMapRenderer().render(matrices, vertexConsumers, mapid, mapState, false, light);
            //#else
            //$$ client.gameRenderer.getMapRenderer().render(matrices, vertexConsumers, mapState, false, light);
            //#endif
            //spotless:on
        }
    }

    public static void addVertex(VertexConsumer cons, Matrix4f matrix4f, float x, float y, float z, float u, float v,
            int lightmapUV) {
        // spotless:off
        //#if MC >= 12100
        cons.addVertex(matrix4f, x, y, z).setColor(-1).setUv(u, v).setLight(lightmapUV);
        //#else
        //$$ cons.vertex(matrix4f, x, y, z).color(255, 255, 255, 255).uv(u, v).uv2(lightmapUV)
        //$$ .endVertex();
        //#endif
        //spotless:on
    }

}
