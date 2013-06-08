package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.roboclaw.RoboclawProxy;

import java.io.IOException;
import java.util.logging.Logger;

public class AmberNaviDrive extends NaviDriveHelper {

    // radius of robo in [mm] paoolo TODO get radius of robo
    public static final int RADIUS = 250;

    // max speed in rotation in [mm/s]
    public static final int MAX_SPEED = 100;

    private static final Logger logger = Logger.getLogger(String.valueOf(AmberNaviDrive.class));

    private final RoboclawProxy roboclawProxy;

    public AmberNaviDrive(RoboclawProxy roboclawProxy) throws IOException {
        this.roboclawProxy = roboclawProxy;
    }

    @Override
    public void stop() {
        drive(0);
    }

    @Override
    public void drive(int speed) {
        drive(speed, speed);
    }

    @Override
    public void drive(int leftSpeed, int rightSpeed) {
        try {
            synchronized (roboclawProxy) {
                roboclawProxy.sendMotorsCommand(leftSpeed, rightSpeed, leftSpeed, rightSpeed);
            }
        } catch (IOException e) {
            logger.info("Error during driving: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rotate(double angle) {
        int time = (int) ((Math.abs(angle) * RADIUS) / MAX_SPEED);
        int speed = (angle > 0 ? MAX_SPEED : -MAX_SPEED);

        drive(speed, -speed);

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.warning("Sleeping interrupted during rotating: " + e.getMessage());
        }

        stop();
    }
}
