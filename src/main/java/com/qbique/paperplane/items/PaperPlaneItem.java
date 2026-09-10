package com.qbique.paperplane.items;

public class PaperPlaneItem extends AbstractPaperPlaneItem {
    // Attribut pour stocker les propriétés de l'item
    private final DyeColor _color;


    // Constructeur de la classe PaperPlaneItem
    public PaperPlaneItem(Properties properties, DyeColor color) {
        super(properties);
        this._color = color; // On stocke la couleur de l'avion en papier
    }

    // Implémentation de la méthode abstraite pour créer une entité paper plane spécifique
    // @Override
    // protected Entity createPaperPlaneEntity(Level level, double x, double y, double z) {
    //     return new PaperPlaneEntity(level, x, y, z);
    // }

}
