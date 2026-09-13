package com.qbique.paperplane.network;

import com.qbique.paperplane.entity.PaperPlaneEntity;
import com.qbique.paperplane.entity.PilotRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

// Envoyé du client au serveur quand le joueur appuie sur la touche "Quitter le vol"
// (voir KeyBindings côté client). Ne contient aucune donnée : le serveur retrouve
// l'avion piloté par l'expéditeur via PilotRegistry.
public class ExitFlightPacket {

    public ExitFlightPacket() {
    }

    public static void encode(ExitFlightPacket packet, FriendlyByteBuf buf) {
        // rien à encoder
    }

    public static ExitFlightPacket decode(FriendlyByteBuf buf) {
        return new ExitFlightPacket();
    }

    public static void handle(ExitFlightPacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender == null) {
                return;
            }
            PaperPlaneEntity plane = PilotRegistry.getPlane(sender.getUUID());
            if (plane != null) {
                // Retire le contrôleur : l'avion continue de voler tout seul (inertie + gravité)
                // au lieu d'être supprimé, jusqu'à un atterrissage/collision normal.
                plane.SetController(null);
                PilotRegistry.clearPilot(sender.getUUID());
            }
            // Rend la caméra au joueur immédiatement (l'avion ne "meurt" plus forcément tout de
            // suite, donc le watchdog client basé sur la disparition de l'entité ne suffit pas ici).
            NetworkHandler.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> sender),
                    new SetCameraPacket(-1)
            );
        });
        ctx.setPacketHandled(true);
    }
}
