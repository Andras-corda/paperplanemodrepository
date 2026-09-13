package com.qbique.paperplane.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.qbique.paperplane.PaperPlane;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.lwjgl.glfw.GLFW;

// Touche configurable (menu Options > Contrôles) pour quitter le pilotage de l'avion en vol.
@Mod.EventBusSubscriber(modid = PaperPlane.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KeyBindings {

    private static final String CATEGORY = "key.categories." + PaperPlane.MODID;

    // X par défaut : évite tout conflit avec les touches de vol (Z/S accélérer-freiner,
    // espace/shift portance-piqué) qui, elles, restent lues en continu pendant le pilotage.
    public static final KeyMapping EXIT_FLIGHT = new KeyMapping(
            "key." + PaperPlane.MODID + ".exit_flight",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_X),
            CATEGORY
    );

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(EXIT_FLIGHT);
    }
}
