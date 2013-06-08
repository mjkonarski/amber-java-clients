package pl.edu.agh.amber.navi.agent;

import pl.edu.agh.amber.navi.drive.NaviDriveHelper;
import pl.edu.agh.amber.navi.dto.NaviVisibility;

import java.util.logging.Logger;

public class NaviDriveAgent implements Runnable {

    private static final Logger logger = Logger.getLogger(String.valueOf(NaviDriveAgent.class));


    private final NaviDriveHelper driveHelper;

    private final Object lock = new Object();

    private double left = 0.0, right = 0.0;

    private boolean running = true;

    public NaviDriveAgent(NaviDriveHelper driveHelper) {
        this.driveHelper = driveHelper;
    }

    public void drive(double left, double right) {
        synchronized (lock) {
            this.left = left;
            this.right = right;
        }
    }

    public void changeDrive(double left, double right) {
        synchronized (lock) {
            this.left += left;
            this.right += right;
        }
    }

    public void speedUp() {
        changeDrive(10, 10);
    }

    public void speedDown() {
        changeDrive(-10, -10);
    }

    public void turnLeft() {
        changeDrive(-5, 5);
    }

    public void turnRight() {
        changeDrive(5, -5);
    }

    public void rotateLeft() {
        driveHelper.rotate(-5);
    }

    public void rotateRight() {
        driveHelper.rotate(5);
    }

    synchronized void stop() {
        running = false;
    }

    synchronized boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        double left, right, oldLeft = 0.0, oldRight = 0.0;
        NaviVisibility visibility;

        try {
            while (isRunning()) {
                synchronized (lock) {
                    left = this.left;
                    right = this.right;
                }
                if (Double.compare(left, oldLeft) != 0
                        || Double.compare(right, oldRight) != 0) {
                    driveHelper.drive((int) left, (int) right);
                    oldLeft = left;
                    oldRight = right;
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            logger.warning("NaviDriveAgent interrupted: " + e.getMessage());
            e.printStackTrace();

            driveHelper.stop();
        }
    }
}
