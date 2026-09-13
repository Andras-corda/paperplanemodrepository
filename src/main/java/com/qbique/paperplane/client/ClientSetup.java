package com.qbique.paperplane.client;

import com.qbique.paperplane.PaperPlane;
import com.qbique.paperplane.client.renderer.PaperPlaneEntityModel;
import com.qbique.paperplane.client.renderer.PaperPlaneEntityRenderer;
import com.qbique.paperplane.registry.ModEntities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Enregistrement des éléments côté client (rendu de l'entité paper plane)
@Mod.EventBusSubscriber(modid = PaperPlane.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PaperPlaneEntityModel.LAYER_LOCATION, PaperPlaneEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.PAPER_PLANE.get(), PaperPlaneEntityRenderer::new);
    }
}
