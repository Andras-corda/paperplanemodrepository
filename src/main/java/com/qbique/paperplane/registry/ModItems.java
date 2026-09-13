package com.qbique.paperplane.registry;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.qbique.paperplane.PaperPlane;
import com.qbique.paperplane.entity.PaperplaneVariant;
import com.qbique.paperplane.items.PaperPlaneItem;

// Classe pour gérer l'enregistrement des items du mod
public class ModItems {
    // déclaration d'un DeferredRegister pour les items
    // Sert à enregistrer les items du mod dans le registre de Forge
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, PaperPlane.MODID);

    // Un item par couleur, en version normale...
    public static final RegistryObject<Item> RED_PAPER_PLANE = registerPlane("red_paper_plane", PaperplaneVariant.RED);
    public static final RegistryObject<Item> ORANGE_PAPER_PLANE = registerPlane("orange_paper_plane", PaperplaneVariant.ORANGE);
    public static final RegistryObject<Item> YELLOW_PAPER_PLANE = registerPlane("yellow_paper_plane", PaperplaneVariant.YELLOW);
    public static final RegistryObject<Item> LIME_PAPER_PLANE = registerPlane("lime_paper_plane", PaperplaneVariant.LIME);
    public static final RegistryObject<Item> GREEN_PAPER_PLANE = registerPlane("green_paper_plane", PaperplaneVariant.GREEN);
    public static final RegistryObject<Item> CYAN_PAPER_PLANE = registerPlane("cyan_paper_plane", PaperplaneVariant.CYAN);
    public static final RegistryObject<Item> LIGHT_BLUE_PAPER_PLANE = registerPlane("light_blue_paper_plane", PaperplaneVariant.LIGHT_BLUE);
    public static final RegistryObject<Item> BLUE_PAPER_PLANE = registerPlane("blue_paper_plane", PaperplaneVariant.BLUE);
    public static final RegistryObject<Item> PURPLE_PAPER_PLANE = registerPlane("purple_paper_plane", PaperplaneVariant.PURPLE);
    public static final RegistryObject<Item> MAGENTA_PAPER_PLANE = registerPlane("magenta_paper_plane", PaperplaneVariant.MAGENTA);
    public static final RegistryObject<Item> PINK_PAPER_PLANE = registerPlane("pink_paper_plane", PaperplaneVariant.PINK);
    public static final RegistryObject<Item> BROWN_PAPER_PLANE = registerPlane("brown_paper_plane", PaperplaneVariant.BROWN);
    public static final RegistryObject<Item> WHITE_PAPER_PLANE = registerPlane("white_paper_plane", PaperplaneVariant.WHITE);
    public static final RegistryObject<Item> LIGHT_GRAY_PAPER_PLANE = registerPlane("light_gray_paper_plane", PaperplaneVariant.LIGHT_GRAY);
    public static final RegistryObject<Item> GRAY_PAPER_PLANE = registerPlane("gray_paper_plane", PaperplaneVariant.GRAY);
    public static final RegistryObject<Item> BLACK_PAPER_PLANE = registerPlane("black_paper_plane", PaperplaneVariant.BLACK);

    // ... et un par couleur en version explosive
    public static final RegistryObject<Item> EXPLOSIVE_RED_PAPER_PLANE = registerPlane("explosive_red_paper_plane", PaperplaneVariant.RED_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_ORANGE_PAPER_PLANE = registerPlane("explosive_orange_paper_plane", PaperplaneVariant.ORANGE_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_YELLOW_PAPER_PLANE = registerPlane("explosive_yellow_paper_plane", PaperplaneVariant.YELLOW_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_LIME_PAPER_PLANE = registerPlane("explosive_lime_paper_plane", PaperplaneVariant.LIME_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_GREEN_PAPER_PLANE = registerPlane("explosive_green_paper_plane", PaperplaneVariant.GREEN_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_CYAN_PAPER_PLANE = registerPlane("explosive_cyan_paper_plane", PaperplaneVariant.CYAN_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_LIGHT_BLUE_PAPER_PLANE = registerPlane("explosive_light_blue_paper_plane", PaperplaneVariant.LIGHT_BLUE_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_BLUE_PAPER_PLANE = registerPlane("explosive_blue_paper_plane", PaperplaneVariant.BLUE_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_PURPLE_PAPER_PLANE = registerPlane("explosive_purple_paper_plane", PaperplaneVariant.PURPLE_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_MAGENTA_PAPER_PLANE = registerPlane("explosive_magenta_paper_plane", PaperplaneVariant.MAGENTA_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_PINK_PAPER_PLANE = registerPlane("explosive_pink_paper_plane", PaperplaneVariant.PINK_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_BROWN_PAPER_PLANE = registerPlane("explosive_brown_paper_plane", PaperplaneVariant.BROWN_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_WHITE_PAPER_PLANE = registerPlane("explosive_white_paper_plane", PaperplaneVariant.WHITE_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_LIGHT_GRAY_PAPER_PLANE = registerPlane("explosive_light_gray_paper_plane", PaperplaneVariant.LIGHT_GRAY_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_GRAY_PAPER_PLANE = registerPlane("explosive_gray_paper_plane", PaperplaneVariant.GRAY_EXPLOSIVE);
    public static final RegistryObject<Item> EXPLOSIVE_BLACK_PAPER_PLANE = registerPlane("explosive_black_paper_plane", PaperplaneVariant.BLACK_EXPLOSIVE);

    // Liste pratique pour l'onglet créatif et pour l'ajout dans le menu vanilla "Ingrédients"
    public static final List<Supplier<Item>> ALL_PAPER_PLANES = List.of(
        RED_PAPER_PLANE, ORANGE_PAPER_PLANE, YELLOW_PAPER_PLANE, LIME_PAPER_PLANE,
        GREEN_PAPER_PLANE, CYAN_PAPER_PLANE, LIGHT_BLUE_PAPER_PLANE, BLUE_PAPER_PLANE,
        PURPLE_PAPER_PLANE, MAGENTA_PAPER_PLANE, PINK_PAPER_PLANE, BROWN_PAPER_PLANE,
        WHITE_PAPER_PLANE, LIGHT_GRAY_PAPER_PLANE, GRAY_PAPER_PLANE, BLACK_PAPER_PLANE,
        EXPLOSIVE_RED_PAPER_PLANE, EXPLOSIVE_ORANGE_PAPER_PLANE, EXPLOSIVE_YELLOW_PAPER_PLANE, EXPLOSIVE_LIME_PAPER_PLANE,
        EXPLOSIVE_GREEN_PAPER_PLANE, EXPLOSIVE_CYAN_PAPER_PLANE, EXPLOSIVE_LIGHT_BLUE_PAPER_PLANE, EXPLOSIVE_BLUE_PAPER_PLANE,
        EXPLOSIVE_PURPLE_PAPER_PLANE, EXPLOSIVE_MAGENTA_PAPER_PLANE, EXPLOSIVE_PINK_PAPER_PLANE, EXPLOSIVE_BROWN_PAPER_PLANE,
        EXPLOSIVE_WHITE_PAPER_PLANE, EXPLOSIVE_LIGHT_GRAY_PAPER_PLANE, EXPLOSIVE_GRAY_PAPER_PLANE, EXPLOSIVE_BLACK_PAPER_PLANE
    );

    private static RegistryObject<Item> registerPlane(String name, PaperplaneVariant variant) {
        return ITEMS.register(name, () -> new PaperPlaneItem(variant, new Item.Properties()));
    }

    // Cette méthode est appelée pour enregistrer les items du mod
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
