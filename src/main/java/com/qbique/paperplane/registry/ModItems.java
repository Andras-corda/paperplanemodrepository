package com.qbique.paperplane.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.qbique.paperplane.PaperPlane;

// Classe pour gérer l'enregistrement des items du mod
public class ModItems {
    // déclaration d'un DeferredRegister pour les items
    // Sert à enregistrer les items du mod dans le registre de Forge
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, PaperPlane.MODID);
        
    public static final RegistryObject<Item> PAPER_PLANE = ITEMS.register("paper_plane", 
        () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EXPLOSIVE_PAPER_PLANE = ITEMS.register("explosive_paper_plane", 
        () -> new Item(new Item.Properties()));

    // Cette méthode est appelée pour enregistrer les items du mod    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
