package com.qbique.paperplane.network;

import com.qbique.paperplane.PaperPlane;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PaperPlane.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public static void register() {
        CHANNEL.registerMessage(nextId++, SetCameraPacket.class,
                SetCameraPacket::encode, SetCameraPacket::decode, SetCameraPacket::handle);
        CHANNEL.registerMessage(nextId++, ExitFlightPacket.class,
                ExitFlightPacket::encode, ExitFlightPacket::decode, ExitFlightPacket::handle);
        CHANNEL.registerMessage(nextId++, PlaneInputPacket.class,
                PlaneInputPacket::encode, PlaneInputPacket::decode, PlaneInputPacket::handle);
    }
}
