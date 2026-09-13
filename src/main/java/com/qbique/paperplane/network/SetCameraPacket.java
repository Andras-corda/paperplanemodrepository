package com.qbique.paperplane.network;

import com.qbique.paperplane.client.ClientCameraHandler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// Envoyé du serveur au client qui vient de lancer un avion en papier :
// demande au client de faire pivoter sa caméra sur l'entité (le corps du joueur ne bouge pas).
public class SetCameraPacket {
    private final int entityId;

    public SetCameraPacket(int entityId) {
        this.entityId = entityId;
    }

    public static void encode(SetCameraPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
    }

    public static SetCameraPacket decode(FriendlyByteBuf buf) {
        return new SetCameraPacket(buf.readInt());
    }

    public static void handle(SetCameraPacket packet, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() ->
                // Passe par DistExecutor pour ne jamais référencer de classe client
                // (Minecraft, ...) depuis un chemin de code chargé aussi côté serveur.
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientCameraHandler.setCameraToEntity(packet.entityId))
        );
        ctx.setPacketHandled(true);
    }
}
