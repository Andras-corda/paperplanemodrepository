package com.qbique.paperplane.registry;

import com.qbique.paperplane.PaperPlane;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(
        ForgeRegistries.ENTITY_TYPES, PaperPlane.MODID
    );

    public static final RegistryObject<EntityType<PaperPlane>> PAPER_PLANE 
    = ENTITIES.register(
        "paper_plane", () -> EntityType.Builder.of(
            PaperPlane::new, 
            MobCategory.MISC
        )
        .sized(0.6F, 0.2F)
        .clientTrackingRange(64)
        .updateInterval(1)
        .build("paper_plane")
    );
}
