package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.navi.dto.NaviMovement;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;

import java.io.IOException;
import java.util.logging.Logger;

public class AmberNaviDrive extends NaviDriveHelper {

    // paoolo TODO get radius of robo
    public static final int RADIUS = 10;

    // paoolo TODO determine metrics of speed
    public static final int MAX_SPEED = 10;

    private static final Logger logger = Logger.getLogger(String.valueOf(AmberNaviDrive.class));

    private final RoboclawProxy roboclawProxy;

    public AmberNaviDrive(RoboclawProxy roboclawProxy) throws IOException {
        this.roboclawProxy = roboclawProxy;
    }

    @Override
    public void drive(NaviMovement movement) {
        int distance = movement.getLength(), time = movement.getTime();
        int speed = distance / time;

        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
            time = distance / MAX_SPEED;
        }

        rotate(movement.getAngle());
        drive(speed);

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            logger.warning("Sleeping interrupted during driving: " + e.getMessage());
        }

        stop();
    }

    @Override
    public void drive(int speed) {
        try {
            roboclawProxy.sendMotorsCommand(speed, speed, speed, speed);

        } catch (IOException e) {
            logger.info("Error during driving: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rotate(int angle) {
        try {
            int time = (Math.abs(angle) * RADIUS) / MAX_SPEED;
            int speed = (angle > 0 ? MAX_SPEED : -MAX_SPEED);

            roboclawProxy.sendMotorsCommand(speed, -speed, speed, -speed);

            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                logger.warning("Sleeping interrupted during rotating: " + e.getMessage());
            }

            stop();

        } catch (IOException e) {
            logger.warning("Error during rotating: " + e.getMessage());
            throw new RuntimeException(e);

        }
    }

    @Override
    public void stop() {
        drive(0);
    }
}
