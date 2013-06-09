package pl.edu.agh.amber.navi.agent;

import pl.edu.agh.amber.navi.NaviConst;
import pl.edu.agh.amber.navi.drive.NaviDriveHelper;

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

    public void changeAngleTo(double angle) {
        angle = Math.toRadians(angle);
        double val = Math.tan(angle) * NaviConst.SPACING;
        synchronized (lock) {
            // paoolo TODO check it
            double diff = val - (right - left);
            right += diff / 2;
            left -= diff / 2;
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
        int left, right, oldLeft = 0, oldRight = 0;

        try {
            while (isRunning()) {
                synchronized (lock) {
                    left = (int) this.left;
                    right = (int) this.right;
                }
                System.err.println("Current speed: " + left + ", " + right);
                if (left != oldLeft || right != oldRight) {
                    driveHelper.drive(left, right);
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
