package com.qbique.paperplane.entity;

import com.qbique.paperplane.PaperPlane;

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

import javax.annotation.Nullable;
import java.util.UUID;

// Le corps du joueur reste où il a lancé l'avion : seule sa caméra bascule sur l'entité
// (voir PaperPlaneItem#use et le paquet réseau SetCameraPacket / le watchdog client ClientEvents).
public class PaperPlaneEntity extends Entity {
    /// Synchro les variantes de couleurs
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(
            PaperPlaneEntity.class,
            EntityDataSerializers.INT);

    private UUID controllerUUID;

    // État des touches + regard du pilote (voir PlaneInputPacket), lu uniquement côté serveur.
    // Le regard doit être synchronisé explicitement : tant que la caméra du client est sur
    // l'avion, LocalPlayer#sendPosition() (vanilla) n'envoie plus jamais la rotation du joueur
    // au serveur (isControlledCamera() devient faux), donc player.getYRot()/getXRot() resteraient
    // figés côté serveur sans ce paquet.
    private boolean inputForward;
    private boolean inputBackward;
    private boolean inputUp;
    private boolean inputDown;
    private float pilotYaw;
    private float pilotPitch;

    // Multiplicateur de vitesse ajusté par avancer/freiner, entre THROTTLE_MIN et THROTTLE_MAX
    private double throttle = 1.0D;
    private static final double THROTTLE_MIN = 0.4D;
    private static final double THROTTLE_MAX = 1.5D;
    private static final double THROTTLE_STEP = 0.02D;

    private static final double maxSpeed = 0.6D;

    private static final double turnSpeed = 0.5D;

    private static final double gravity = 0.2D;

    // Touches de déplacement du pilote : Z/W accélère, S freine, espace allège la chute, shift pique plus fort.
    // yaw/pitch : regard actuel du joueur, envoyé explicitement car vanilla ne le synchronise
    // plus tout seul pendant le pilotage (voir le commentaire sur pilotYaw plus haut).
    public void setFlightInput(boolean forward, boolean backward, boolean up, boolean down, float yaw, float pitch) {
        this.inputForward = forward;
        this.inputBackward = backward;
        this.inputUp = up;
        this.inputDown = down;
        this.pilotYaw = yaw;
        this.pilotPitch = pitch;
    }

    public PaperPlaneEntity(EntityType<? extends PaperPlaneEntity> entityType, Level level) {
        super(entityType, level);
    }

    public void SetVariant(PaperplaneVariant variant) {
        this.entityData.set(VARIANT, variant.getID());
    }

    public PaperplaneVariant getVariant() {
        return PaperplaneVariant.fromid(this.entityData.get(VARIANT));
    }

    // Qui "pilote" l'avion (via sa caméra) — n'a aucun lien avec la position du joueur
    public void SetController(@Nullable Player player) {
        this.controllerUUID = player == null ? null : player.getUUID();
    }

    @Nullable
    public Player getController() {
        if (controllerUUID == null) {
            return null;
        }
        return this.level().getPlayerByUUID(controllerUUID);
    }

    @Nullable
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
        PaperplaneVariant variant = getVariant();
        Player player = getController();
        Vec3 currentVel = this.getDeltaMovement();
        Vec3 newVel;

        double effectiveGravity = variant.getGravity();

        if (player != null) {
            // avancer/freiner : ajuste le régime moteur, entre THROTTLE_MIN et THROTTLE_MAX
            if (inputForward) {
                throttle = Math.min(THROTTLE_MAX, throttle + THROTTLE_STEP);
            } else if (inputBackward) {
                throttle = Math.max(THROTTLE_MIN, throttle - THROTTLE_STEP);
            }

            // espace : un peu de portance (moins de chute) / shift : piqué plus prononcé
            if (inputUp) {
                effectiveGravity *= 0.3D;
            } else if (inputDown) {
                effectiveGravity *= 2.0D;
            }

            // piloté : vise la direction du regard synchronisé du joueur (pilotYaw/pilotPitch,
            // PAS player.getLookAngle() qui reste figé pendant le pilotage, voir plus haut),
            // à la vitesse actuelle du régime moteur
            Vec3 targetDir = this.calculateViewVector(pilotPitch, pilotYaw).normalize();
            double planeSpeed = variant.getSpeed() * throttle;
            Vec3 targetVel = targetDir.scale(planeSpeed);

            if (currentVel.lengthSqr() < 0.001D) {
                currentVel = targetVel;
            }
            newVel = currentVel.lerp(targetVel, turnSpeed);
            // Ré-échelonne à la vitesse visée après l'interpolation : un lerp linéaire entre deux
            // vecteurs raccourcit fortement le résultat au milieu d'un grand virage (l'avion
            // "calait" pile pendant les virages sur le côté). On garde ainsi une vitesse quasi
            // constante pendant le virage, seule la direction met du temps à suivre.
            if (newVel.lengthSqr() > 1.0E-6D) {
                newVel = newVel.normalize().scale(planeSpeed);
            }

            // la caméra (qui lit la rotation de l'entité) suit directement le regard synchronisé,
            // sans lissage, pour un retour instantané à la souris
            this.setYRot(pilotYaw);
            this.setXRot(pilotPitch);
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();

            // LOG debug : décommente/enlève une fois le pilotage vérifié
            if (this.tickCount % 5 == 0) {
                PaperPlane.LOGGER.info(
                        "[PaperPlane] tick={} pilotYaw={} pilotPitch={} (player.getYRot()={}, figé pendant le pilotage) lookDir={} throttle={} input(fwd={},back={},up={},down={}) targetVel={} newVel={}",
                        this.tickCount, pilotYaw, pilotPitch, player.getYRot(), targetDir,
                        String.format("%.2f", throttle),
                        inputForward, inputBackward, inputUp, inputDown,
                        targetVel, newVel
                );
            }
        } else {
            // plus de pilote (touche "quitter le vol", déconnexion...) : l'avion garde son élan,
            // ne subit plus que la gravité, et continue jusqu'à un atterrissage/collision normal
            newVel = currentVel;
        }

        newVel = newVel.add(0.0D, -effectiveGravity, 0.0D);
        double speed = newVel.length();

        if (speed > maxSpeed) {
            newVel = newVel.normalize().scale(maxSpeed);
        }
        // deplacement objet
        this.move(MoverType.SELF, newVel);

        if (player == null) {
            // sans pilote, le modèle s'oriente selon sa trajectoire réelle plutôt que le regard
            updateRotationFromVelocity(newVel);
        }

        // collision
        if (this.horizontalCollision || this.verticalCollision) {
            if (variant.getisExplosive()) {
                explode();
            }
            discardAndReleaseControl();
            return;
        }
        if (this.onGround()) {
            if (variant.getisExplosive()) {
                explode();
            }
            discardAndReleaseControl();
        }
    }

    // Retire l'avion du monde et libère l'entrée du registre de pilotage (voir PilotRegistry / ExitFlightPacket)
    private void discardAndReleaseControl() {
        PilotRegistry.clearPilot(controllerUUID);
        this.discard();
    }

    private void updateRotationFromVelocity(Vec3 velocity) {
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
        tag.putInt("Variant", getVariant().getID());
        if (controllerUUID != null) {
            tag.putUUID("Controller", controllerUUID);
        }
    }

    // Chargement des NBT
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Variant")) {
            int id = tag.getInt("Variant");
            SetVariant(PaperplaneVariant.fromid(id));
        }
        if (tag.hasUUID("Controller")) {
            controllerUUID = tag.getUUID("Controller");
        }
    }

    // Spawn packet
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
