package com.qbique.paperplane.network;

import com.qbique.paperplane.entity.PaperPlaneEntity;
import com.qbique.paperplane.entity.PilotRegistry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// Envoyé du client au serveur à chaque tick pendant le pilotage (voir ClientEvents) :
// état des touches de déplacement + regard actuel du joueur.
//
// Pourquoi le regard est ici (et pas lu via player.getYRot()/getXRot() côté serveur) :
// LocalPlayer#sendPosition() (vanilla) n'envoie plus AUCUNE mise à jour de position/rotation
// au serveur dès que la caméra du client n'est plus le joueur (isControlledCamera() devient
// faux avec Minecraft.setCameraEntity(avion)). Le regard du joueur, côté serveur, resterait
// donc figé pendant tout le pilotage si on ne le synchronisait pas nous-mêmes ici.
public class PlaneInputPacket {
    private final boolean forward;
    private final boolean backward;
    private final boolean up;
    private final boolean down;
    private final float yaw;
    private final float pitch;

    public PlaneInputPacket(boolean forward, boolean backward, boolean up, boolean down, float yaw, float pitch) {
        this.forward = forward;
        this.backward = backward;
        this.up = up;
        this.down = down;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public static void encode(PlaneInputPacket packet, FriendlyByteBuf buf) {
        int flags = (packet.forward ? 1 : 0) | (packet.backward ? 2 : 0) | (packet.up ? 4 : 0) | (packet.down ? 8 : 0);
        buf.writeByte(flags);
        buf.writeFloat(packet.yaw);
        buf.writeFloat(packet.pitch);
    }

    public static PlaneInputPacket decode(FriendlyByteBuf buf) {
        int flags = buf.readByte();
        float yaw = buf.readFloat();
        float pitch = buf.readFloat();
        return new PlaneInputPacket((flags & 1) != 0, (flags & 2) != 0, (flags & 4) != 0, (flags & 8) != 0, yaw, pitch);
    }

    public static void handle(PlaneInputPacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer sender = ctx.getSender();
            if (sender == null) {
                return;
            }
            PaperPlaneEntity plane = PilotRegistry.getPlane(sender.getUUID());
            if (plane != null) {
                plane.setFlightInput(packet.forward, packet.backward, packet.up, packet.down, packet.yaw, packet.pitch);
            }
        });
        ctx.setPacketHandled(true);
    }
}
