package com.qbique.paperplane.entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Registre côté serveur : quel avion chaque joueur pilote actuellement.
// Permet à ExitFlightPacket de retrouver l'avion à abandonner sans parcourir le niveau.
public class PilotRegistry {

    private static final Map<UUID, PaperPlaneEntity> ACTIVE_PLANES = new ConcurrentHashMap<>();

    private PilotRegistry() {
    }

    public static void setPilot(UUID playerId, PaperPlaneEntity plane) {
        ACTIVE_PLANES.put(playerId, plane);
    }

    public static void clearPilot(UUID playerId) {
        if (playerId != null) {
            ACTIVE_PLANES.remove(playerId);
        }
    }

    public static PaperPlaneEntity getPlane(UUID playerId) {
        return ACTIVE_PLANES.get(playerId);
    }
}
