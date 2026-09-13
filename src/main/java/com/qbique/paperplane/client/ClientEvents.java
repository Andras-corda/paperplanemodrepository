package com.qbique.paperplane.client;

import com.qbique.paperplane.PaperPlane;
import com.qbique.paperplane.entity.PaperPlaneEntity;
import com.qbique.paperplane.network.ExitFlightPacket;
import com.qbique.paperplane.network.NetworkHandler;
import com.qbique.paperplane.network.PlaneInputPacket;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Sur le bus Forge (pas le bus du mod) : surveille la caméra à chaque tick client.
@Mod.EventBusSubscriber(modid = PaperPlane.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    // Filet de sécurité : si l'avion sur lequel la caméra est posée disparaît (atterrissage,
    // explosion, /kill, déconnexion...), on rend la caméra au joueur automatiquement.
    // Cela évite toute caméra bloquée, sans avoir besoin d'un paquet réseau de "reset" explicite.
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        boolean piloting = minecraft.getCameraEntity() instanceof PaperPlaneEntity plane && plane.isAlive();

        if (minecraft.getCameraEntity() instanceof PaperPlaneEntity plane && !plane.isAlive()) {
            minecraft.setCameraEntity(minecraft.player);
        }

        // Touche configurable pour quitter le vol volontairement (menu Options > Contrôles)
        while (KeyBindings.EXIT_FLIGHT.consumeClick()) {
            NetworkHandler.CHANNEL.sendToServer(new ExitFlightPacket());
        }

        // Réutilise les touches de déplacement du joueur (Options > Contrôles) pour piloter
        // l'avion tant que la caméra est dessus : la souris continue de diriger le cap.
        if (piloting) {
            boolean forward = minecraft.options.keyUp.isDown();
            boolean backward = minecraft.options.keyDown.isDown();
            boolean up = minecraft.options.keyJump.isDown();
            boolean down = minecraft.options.keyShift.isDown();
            NetworkHandler.CHANNEL.sendToServer(new PlaneInputPacket(
                    forward, backward, up, down,
                    minecraft.player.getYRot(), minecraft.player.getXRot()
            ));

            // LOG debug : décommente/enlève une fois le pilotage vérifié
            if (minecraft.level != null && minecraft.level.getGameTime() % 5 == 0) {
                PaperPlane.LOGGER.info(
                        "[PaperPlane][client] cameraEntity={} playerYaw={} playerPitch={} input(fwd={},back={},up={},down={})",
                        minecraft.getCameraEntity(), minecraft.player.getYRot(), minecraft.player.getXRot(),
                        forward, backward, up, down
                );
            }
        }
    }
}
