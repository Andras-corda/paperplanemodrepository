package com.qbique.paperplane.client;

import com.qbique.paperplane.PaperPlane;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientCameraHandler {

    private ClientCameraHandler() {
    }

    // Fait regarder le client à travers l'entité désignée, sans toucher à la position du joueur.
    // entityId < 0 : rend la caméra au joueur (utilisé quand celui-ci quitte volontairement le vol).
    public static void setCameraToEntity(int entityId) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return;
        }
        if (entityId < 0) {
            PaperPlane.LOGGER.info("[PaperPlane][client] SetCameraPacket(-1) : retour de la caméra sur le joueur");
            if (minecraft.player != null) {
                minecraft.setCameraEntity(minecraft.player);
            }
            return;
        }
        Entity entity = minecraft.level.getEntity(entityId);
        PaperPlane.LOGGER.info("[PaperPlane][client] SetCameraPacket({}) : entité trouvée = {}", entityId, entity);
        if (entity != null) {
            minecraft.setCameraEntity(entity);
        }
    }
}
