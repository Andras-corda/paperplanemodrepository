package com.qbique.paperplane.items;

import com.qbique.paperplane.entity.PaperplaneVariant; 
import com.qbique.paperplane.entity.PaperPlane;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
            PaperPlane plane = new PaperPlane(ModEntities.PAPER_PLANE.get(), level);

            // initial pos
            plane.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

            // set variation
            plane.SetVariant(variant);

            // set player controller
            plane.SetController(player);

            plane.setDeltaMovement(look.scale(variant.getSpeed()));

            // world Instanciation
            level.addFreshEntity(plane);

            // Item consumption
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
    
}
