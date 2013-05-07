package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.navi.NaviHelper;
import pl.edu.agh.amber.navi.dto.NaviMovement;

import java.io.IOException;

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

    public abstract void change(NaviMovement movement) throws IOException;
}
