package com.qbique.paperplane.entity;

public enum PaperplaneVariant {
    RED(0, "red", 0.50D, 0.015D, false),
    RED_EXPLOSIVE(1, "red", 0.50D, 0.2D, true),

    ORANGE(2, "orange", 0.50D, 0.015D, false),
    ORANGE_EXPLOSIVE(3, "orange", 0.50D, 0.2D, true),

    YELLOW(4, "yellow", 0.50D, 0.015D, false),
    YELLOW_EXPLOSIVE(5, "yellow", 0.50D, 0.2D, true),

    LIME(6, "lime", 0.50D, 0.015D, false),
    LIME_EXPLOSIVE(7, "lime", 0.50D, 0.2D, true),

    GREEN(8, "green", 0.50D, 0.015D, false),
    GREEN_EXPLOSIVE(9, "green", 0.50D, 0.2D, true),

    CYAN(10, "cyan", 0.50D, 0.015D, false),
    CYAN_EXPLOSIVE(11, "cyan", 0.50D, 0.2D, true),

    LIGHT_BLUE(12, "light_blue", 0.50D, 0.015D, false),
    LIGHT_BLUE_EXPLOSIVE(13, "light_blue", 0.50D, 0.2D, true),

    BLUE(14, "blue", 0.50D, 0.015D, false),
    BLUE_EXPLOSIVE(15, "blue", 0.50D, 0.2D, true),

    PURPLE(16, "purple", 0.50D, 0.015D, false),
    PURPLE_EXPLOSIVE(17, "purple", 0.50D, 0.2D, true),

    MAGENTA(18, "magenta", 0.50D, 0.015D, false),
    MAGENTA_EXPLOSIVE(19, "magenta", 0.50D, 0.2D, true),

    PINK(20, "pink", 0.50D, 0.015D, false),
    PINK_EXPLOSIVE(21, "pink", 0.50D, 0.2D, true),

    BROWN(22, "brown", 0.50D, 0.015D, false),
    BROWN_EXPLOSIVE(23, "brown", 0.50D, 0.2D, true),

    WHITE(24, "white", 0.50D, 0.015D, false),
    WHITE_EXPLOSIVE(25, "white", 0.50D, 0.2D, true),

    LIGHT_GRAY(26, "light_gray", 0.50D, 0.015D, false),
    LIGHT_GRAY_EXPLOSIVE(27, "light_gray", 0.50D, 0.2D, true),

    GRAY(28, "gray", 0.50D, 0.015D, false),
    GRAY_EXPLOSIVE(29, "gray", 0.50D, 0.2D, true),

    BLACK(30, "black", 0.50D, 0.015D, false),
    BLACK_EXPLOSIVE(31, "black", 0.50D, 0.2D, true);

    private final int id;

    private final String textureName; /// chemin des textures à rajouter plus tard

    private final double speed;

    private final double gravity;

    private final boolean isExplosive;

    PaperplaneVariant(int id, String textureName, double speed, double gravity, boolean isExplosive) {
        this.id = id;
        this.textureName = textureName;
        this.speed = speed;
        this.gravity = gravity;
        this.isExplosive = isExplosive;
    }

    public int getID(){return id;}
    public String getTextureName(){return textureName;}
    public double getSpeed(){return speed;}
    public double getGravity(){return gravity;}
    public boolean getisExplosive(){return isExplosive;}

    public static PaperplaneVariant fromid(int id){
        for(PaperplaneVariant variant : values()){
            if (variant.id == id){
                return variant;
            }
        }
        return WHITE;
    }
}
