package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.navi.NaviHelper;
import pl.edu.agh.amber.navi.dto.NaviMovement;

/**
 * Provide access to drive system.
 */
public abstract class NaviDriveHelper extends NaviHelper {

    private final Object movementLock = new Object();

    private NaviMovement movement;

    public NaviMovement getMovement() {
        synchronized (movementLock) {
            return movement;
        }
    }

    @Override
    protected void notifyMovementChange(NaviMovement movement) {
        synchronized (movementLock) {
            this.movement = movement;
        }
        super.notifyMovementChange(movement);
    }

    public abstract void drive(NaviMovement movement, int time);

    public abstract void drive(int speed);

    public abstract void stop();

    public abstract void rotate(double angle);

    public abstract void drive(int leftSpeed, int rightSpeed);
}
