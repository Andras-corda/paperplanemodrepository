package com.qbique.paperplane.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

public class AbstractPaperPlaneItem extends Item {

    // Constructeur de la classe AbstractPaperPlaneItem
    public AbstractPaperPlaneItem(Properties properties) {
        super(properties);
    }

    // Méthode abstraite pour créer une entité paper plane spécifique
    // protected abstract Entity createPaperPlaneEntity(Level level, double x,
    // double y, double z);

    @Override
    public InteractionResult use(
            Level level,
            Player player,
            InteractionHand hand) {
        // Vérification coté serveur pour éviter les problèmes de synchronisation
        if (!level.isClientSide) {
            // On demande à la classe enfant de créer l'entité paper plane spécifique
            // Entity paperPlaneEntity = createPaperPlaneEntity(level, player.getX(),
            // player.getY(), player.getZ());

            // On place l'avion à la position des yeux du joueur
            // paperPlaneEntity.setPos(
                // player.getX(),
                // player.getEyeY() - 0.1,
                // player.getZ()
            // );

            // On ajoute l'entité paper plane au monde
            // level.addFreshEntity(paperPlaneEntity);
        }

        // On indique que l'action a été effectuée avec succès
        return InteractionResult.SUCCESS;

    }
}
