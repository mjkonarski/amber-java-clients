package pl.edu.agh.amber.navi.agent;

import pl.edu.agh.amber.navi.drive.NaviDriveHelper;
import pl.edu.agh.amber.navi.dto.NaviVisibility;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;

import java.util.logging.Logger;

public class NaviDriveAgent implements Runnable {

    private static final Logger logger = Logger.getLogger(String.valueOf(NaviDriveAgent.class));

    private final NaviEyeHelper eyeHelper;

    private final NaviDriveHelper driveHelper;

    private final Object lock = new Object();

    private double left = 0.0, right = 0.0;

    private boolean running = true;

    public NaviDriveAgent(NaviDriveHelper driveHelper, NaviEyeHelper eyeHelper) {
        this.driveHelper = driveHelper;
        this.eyeHelper = eyeHelper;
    }

    public void drive(double left, double right) {
        synchronized (lock) {
            this.left = left;
            this.right = right;
        }
    }

    synchronized void stop() {
        running = false;
    }

    synchronized boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        double left, right, oldLeft, oldRight;
        NaviVisibility visibility;

        while (isRunning()) {
            synchronized (lock) {
                left = this.left;
                right = this.right;
            }

            visibility = eyeHelper.getVisibility();
            if (visibility != null) {
                // paoolo FIXME read speed from driverHelper
                double length = getLength(left, right, visibility);
                // paoolo TODO determine how to change left,right values more, to avoid

            } else {
                logger.warning("No information about visibility");
            }

            oldLeft = left;
            oldRight = right;
        }
    }

    private static double getAngle(double left, double right) {
        // paoolo TODO compute value from left/right vector
        return 0.0;
    }

    private static double getLength(double left, double right, NaviVisibility visibility) {
        double angle = getAngle(left, right);
        return visibility.getLengthForAngle(angle);
    }
}
