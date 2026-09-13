package com.qbique.paperplane.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;

import com.qbique.paperplane.PaperPlane;
import com.qbique.paperplane.entity.PaperPlaneEntity;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class PaperPlaneEntityRenderer extends EntityRenderer<PaperPlaneEntity> {

    private final PaperPlaneEntityModel model;

    public PaperPlaneEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PaperPlaneEntityModel(context.bakeLayer(PaperPlaneEntityModel.LAYER_LOCATION));
    }

    @Override
    public ResourceLocation getTextureLocation(PaperPlaneEntity entity) {
        String texture = entity.getVariant().getTextureName();
        return new ResourceLocation(PaperPlane.MODID, "textures/entity/paper_plane/" + texture + ".png");
    }

    @Override
    public void render(PaperPlaneEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                        MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Oriente le modèle selon la rotation (interpolée) de l'entité, sinon il reste
        // toujours affiché dans son orientation par défaut quelle que soit la direction du vol.
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        // Signe inversé par rapport au pitch brut : en montant (xRot négatif) le nez doit se
        // lever, en piquant (xRot positif) il doit s'abaisser — c'était inversé.
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

        // Retournement obligatoire pour tout modèle ModelPart/CubeListBuilder (voir BoatRenderer,
        // LivingEntityRenderer dans les sources vanilla) : sans lui, le modèle apparaît inversé
        // haut/bas et en miroir gauche/droite — c'était le bug "l'entité est retournée".
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        model.setupAnim(entity, 0.0F, 0.0F, entity.tickCount + partialTicks, entity.getYRot(), entity.getXRot());
        VertexConsumer vertexConsumer = buffer.getBuffer(model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
