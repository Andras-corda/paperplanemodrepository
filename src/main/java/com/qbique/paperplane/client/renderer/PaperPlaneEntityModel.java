package com.qbique.paperplane.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import com.qbique.paperplane.entity.PaperPlaneEntity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

public class PaperPlaneEntityModel
        extends EntityModel<PaperPlaneEntity> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(
                    new ResourceLocation(
                            "paperplane",
                            "paper_plane_entity"
                    ),
                    "main"
            );

    private final ModelPart group;

    public PaperPlaneEntityModel(ModelPart root) {

        this.group =
                root.getChild("group");
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshdefinition =
                new MeshDefinition();

        PartDefinition partdefinition =
                meshdefinition.getRoot();

        PartDefinition group =
                partdefinition.addOrReplaceChild(
                        "group",

                        CubeListBuilder.create()

                                .texOffs(-14, 0)
                                .addBox(
                                        -5.4F,
                                        7.0F,
                                        -7.0F,
                                        4.0F,
                                        0.0F,
                                        14.0F,
                                        new CubeDeformation(0.0F)
                                )

                                .texOffs(-6, 0)
                                .addBox(
                                        1.4F,
                                        7.0F,
                                        -7.0F,
                                        4.0F,
                                        0.0F,
                                        14.0F,
                                        new CubeDeformation(0.0F)
                                ),

                        PartPose.offset(
                                0.0F,
                                16.0F,
                                0.0F
                        )
                );

        group.addOrReplaceChild(
                "cube_r1",

                CubeListBuilder.create()
                        .texOffs(0, 2)
                        .addBox(
                                -1.0F,
                                -1.0F,
                                -7.0F,
                                0.0F,
                                2.0F,
                                14.0F,
                                new CubeDeformation(0.0F)
                        ),

                PartPose.offsetAndRotation(
                        0.0F,
                        7.0F,
                        0.0F,
                        0.0F,
                        0.0F,
                        -0.7854F
                )
        );

        group.addOrReplaceChild(
                "cube_r2",

                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                1.0F,
                                -1.0F,
                                -7.0F,
                                0.0F,
                                2.0F,
                                14.0F,
                                new CubeDeformation(0.0F)
                        ),

                PartPose.offsetAndRotation(
                        0.0F,
                        7.0F,
                        0.0F,
                        0.0F,
                        0.0F,
                        0.7854F
                )
        );

        return LayerDefinition.create(
                meshdefinition,
                64,
                64
        );
    }

    @Override
    public void setupAnim(
            PaperPlaneEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
    }

    @Override
    public void renderToBuffer(
            PoseStack poseStack,
            VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha
    ) {

        group.render(
                poseStack,
                vertexConsumer,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha
        );
    }
}