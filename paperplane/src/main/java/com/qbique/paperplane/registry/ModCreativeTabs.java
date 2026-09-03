package com.qbique.paperplane.registry;

import com.qbique.paperplane.PaperPlane;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = 
    DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PaperPlane.MODID);

    public static final RegistryObject<CreativeModeTab> PAPER_PLANE_TAB = CREATIVE_TABS.register("paper_plane_tab", 
        () -> CreativeModeTab.builder()
            .icon(() -> ModItems.PAPER_PLANE.get().getDefaultInstance()) // Icône de l'onglet
            .title(Component.translatable(PaperPlane.MODID + ".creative_tab")) // Titre de l'onglet
            .displayItems((parameters, output) -> {
                output.accept(ModItems.PAPER_PLANE.get()); // Ajouter l'item "Paper Plane" à l'onglet
                output.accept(ModItems.EXPLOSIVE_PAPER_PLANE.get()); // Ajouter l'item "Explosive Paper Plane" à l'onglet
            })
            .build()); 

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
