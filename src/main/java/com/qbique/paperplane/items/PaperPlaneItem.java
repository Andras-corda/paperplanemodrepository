package com.qbique.paperplane.items;

import com.qbique.paperplane.entity.PaperplaneVariant;
import com.qbique.paperplane.entity.PaperPlaneEntity;
import com.qbique.paperplane.entity.PilotRegistry;
import com.qbique.paperplane.network.NetworkHandler;
import com.qbique.paperplane.network.SetCameraPacket;
import com.qbique.paperplane.registry.ModEntities;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

// Icône 2D classique (item/generated) dans tous les contextes, y compris en main.
public class PaperPlaneItem extends Item {
    private final PaperplaneVariant variant;

    public PaperPlaneItem(PaperplaneVariant variant, Properties properties) {
        super(properties);
        this.variant = variant;
    }

    public PaperplaneVariant getVariant() {
        return variant;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        // server side entity instanciation
        if (!level.isClientSide) {
            
            // Local Player Looking Direction
            Vec3 look = player.getLookAngle().normalize();
            
            // spawn pos of paperplane
            Vec3 spawnPos = player.position().add(look.scale(1.2D)).add(0.0D, 1.0D, 0.0D);

            // spawn entity
            PaperPlaneEntity plane = new PaperPlaneEntity(ModEntities.PAPER_PLANE.get(), level);

            // initial pos
            plane.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

            // set variation
            plane.SetVariant(variant);

            // le joueur reste le "pilote" (son regard dirige l'avion), mais son corps
            // ne bouge pas : seule sa caméra sera basculée sur l'entité (cf. plus bas)
            plane.SetController(player);
            PilotRegistry.setPilot(player.getUUID(), plane);
            // amorce le regard synchronisé avec la valeur actuelle, en attendant le premier
            // PlaneInputPacket du client (sinon l'avion pointerait vers yaw=0/pitch=0 le temps
            // que le paquet arrive)
            plane.setFlightInput(false, false, false, false, player.getYRot(), player.getXRot());

            plane.setDeltaMovement(look.scale(variant.getSpeed()));

            // world Instanciation
            level.addFreshEntity(plane);

            // demande au client du lanceur de faire basculer sa caméra sur l'avion,
            // sans déplacer son corps (celui-ci reste exposé à l'endroit du lancer)
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new SetCameraPacket(plane.getId())
                );
            }

            // Item consumption
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    
}
