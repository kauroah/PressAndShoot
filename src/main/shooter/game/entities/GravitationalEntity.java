package src.main.shooter.game.entities;

import src.main.shooter.game.ServerGame.GameSettings;

public interface GravitationalEntity extends KineticEntity {

    public default double getGravitationalForce() {
        return GameSettings.GLOBAL_GRAVITY;
    }

    public default void applyGravity() {
        shiftYVel(getGravitationalForce());
    }
}
