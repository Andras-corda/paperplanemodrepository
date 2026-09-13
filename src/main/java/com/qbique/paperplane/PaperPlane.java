package com.qbique.paperplane;

import com.mojang.logging.LogUtils;
import com.qbique.paperplane.network.NetworkHandler;
import com.qbique.paperplane.registry.ModCreativeTabs;
import com.qbique.paperplane.registry.ModEntities;
import com.qbique.paperplane.registry.ModItems;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(PaperPlane.MODID)
public class PaperPlane {
    public static final String MODID = "paperplane"; // Identifiant unique du mod
    public static final Logger LOGGER = LogUtils.getLogger(); // Logger partagé (SLF4J, fourni par Forge)
   
    // Constructeur du mod (appelé par Forge)
    public PaperPlane() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus(); // Bus d'événements du mod sert à enregistrer les contenus et les événements liés au mod
        modEventBus.addListener(this::commonSetup); // Enregistrer la méthode de configuration commune

        //
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus); // Enregistrer les onglets créatifs du mod dans le bus d'événements du mod
        ModItems.register(modEventBus); // Enregistrer les items du mod dans le bus d'événements du mod
        ModEntities.ENTITIES.register(modEventBus); // Enregistrer les entités du mod dans le bus d'événements du mod

        NetworkHandler.register(); // Enregistrer le canal réseau (paquet de bascule de caméra)



        MinecraftForge.EVENT_BUS.register(this); // S'abonner au bus d'événements de Forge pour les événements globaux
        modEventBus.addListener(this::AddCreative); // Enregistrer la méthode de configuration côté client
        
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        
    }

    // Ajout d'éléments au menu créatif
    private void AddCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            ModItems.ALL_PAPER_PLANES.forEach(event::accept);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Code à exécuter lorsque le serveur démarre
    }


}