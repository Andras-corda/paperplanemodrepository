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
            .icon(() -> ModItems.WHITE_PAPER_PLANE.get().getDefaultInstance()) // Icône de l'onglet
            .title(Component.translatable("creative_tab." + PaperPlane.MODID)) // Titre de l'onglet
            .displayItems((parameters, output) -> {
                // Toutes les couleurs + l'item explosif
                ModItems.ALL_PAPER_PLANES.forEach(item -> output.accept(item.get()));
            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}
