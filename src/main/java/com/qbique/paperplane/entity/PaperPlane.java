package com.qbique.paperplane.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import java.util.UUID;

public class PaperPlane extends Entity {
    /// Synchro les variantes de couleurs
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(
            PaperPlane.class,
            EntityDataSerializers.INT);

    private UUID controllerUUID;

    private static final double maxSpeed = 0.6D;

    private static final double turnSpeed = 0.5D;

    private static final double gravity = 0.2D;

    public PaperPlane(EntityType<? extends PaperPlane> entityType, Level level) {
        super(entityType, level);
    }

    public void SetVariant(PaperplaneVariant variant) {
        this.entityData.set(VARIANT, variant.getID());
    }

    public PaperplaneVariant getVariant() {
        return PaperplaneVariant.fromid(this.entityData.get(VARIANT));
    }

    public void SetController(Player player) {
        if (player == null) {
            this.controllerUUID = null;
            return;
        }
        this.controllerUUID = player.getUUID();
    }

    public Player getController() {
        if (controllerUUID == null) {
            return null;
        }
        return this.level().getPlayerByUUID(controllerUUID);
    }

    public UUID getControllerUUID() {
        return controllerUUID;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(
                VARIANT, PaperplaneVariant.WHITE.getID());
    }

    @Override
    public void tick() {
        super.tick();
        // physique calculé côté serveur
        if (this.level().isClientSide) {
            return;
        }
        Player player = getController();
        // supprime l'avion si joueur n'est plus dispo
        if (player == null) {
            this.discard();
            return;
        }
        PaperplaneVariant variant = getVariant();
        // check la direction d vision du joueur avec utilisation vecteur
        Vec3 targetDir = player.getLookAngle().normalize();
        // get vitesse
        double planeSpeed = variant.getSpeed();
        Vec3 targetVel = targetDir.scale(planeSpeed);
        Vec3 currentVel = this.getDeltaMovement();

        if (currentVel.lengthSqr() < 0.001D) {
            currentVel = targetVel;
        }
        Vec3 newVel = currentVel.lerp(targetVel, turnSpeed);
        newVel = newVel.add(0.0D, -variant.getGravity(), 0.0D);
        double speed = newVel.length();

        if (speed > maxSpeed) {
            newVel = newVel.normalize().scale(maxSpeed);
        }
        // deplacement objet
        this.move(MoverType.SELF, newVel);
        // rotation du model 3D
        updateRotation(newVel);
        // collision
        if (this.horizontalCollision || this.verticalCollision) {
            if (variant.getisExplosive()) {
                explode();
            }
            this.discard();
            return;
        }
        if (this.onGround()) {
            if (variant.getisExplosive()) {
                explode();
            }
            this.discard();
        }
    }

    private void updateRotation(Vec3 velocity) {
        if (velocity.lengthSqr() < 0.001D) {
            return;
        }

        double hs = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

        // Yaw
        float yaw = (float) (Math.atan2(velocity.z, velocity.x) * (180.0D / Math.PI)) - 90.0F;
        // Pitch
        float pitch = (float) (-Math.atan2(velocity.y, hs) * (180.0D / Math.PI));
        this.setYRot(yaw);
        this.setXRot(pitch);
        this.yRotO = yaw;
        this.xRotO = pitch;
    }

    private void explode() {
        if (this.level().isClientSide) {
            return;
        }
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, Level.ExplosionInteraction.BLOCK);
    }
    // Sauvegarder la variation du pp dans les nbt
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // id
        tag.putInt("Variant", getVariant().getID());

        // joueur controller
        tag.putUUID("Controller", controllerUUID);

    }

    // Chargement des NBT
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        // Contient les variants
        if(tag.contains("Variant")) {
            int id = tag.getInt("Variant");
            SetVariant(PaperplaneVariant.fromid(id));
        }

        // Contient les controller
        if(tag.hasUUID("Controller")) {
            controllerUUID = tag.getUUID("Controller");
        }
    }

    // Spawn packet
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}