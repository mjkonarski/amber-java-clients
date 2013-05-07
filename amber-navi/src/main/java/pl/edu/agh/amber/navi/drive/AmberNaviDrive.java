package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.navi.dto.NaviMovement;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;

import java.io.IOException;
import java.util.logging.Logger;

public class AmberNaviDrive extends NaviDriveHelper {

    private static final Logger logger = Logger.getLogger(String.valueOf(AmberNaviDrive.class));

    private final RoboclawProxy roboclawProxy;

    private double currentAngle, currentSpeed;

    public AmberNaviDrive(RoboclawProxy roboclawProxy) throws IOException {
        this.roboclawProxy = roboclawProxy;
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public void change(NaviMovement movement) throws IOException {
        this.currentAngle += movement.getAngle();
        this.currentSpeed += movement.getSpeed();

        int frontLeft = getFrontLeft(currentAngle, currentSpeed), frontRight = getFrontRight(currentAngle, currentSpeed),
                rearLeft = getRearLeft(currentAngle, currentSpeed), rearRight = getRearRight(currentAngle, currentSpeed);

        roboclawProxy.sendMotorsCommand(frontLeft, frontRight, rearLeft, rearRight);
    }

    private static int getLeft(double angle, double speed) {
        return 0;
    }

    private static int getRight(double angle, double speed) {
        return 0;
    }

    private static int getFrontLeft(double angle, double speed) {
        return getLeft(angle, speed);
    }

    private static int getFrontRight(double angle, double speed) {
        return getRight(angle, speed);
    }

    private static int getRearLeft(double angle, double speed) {
        return getLeft(angle, speed);
    }

    private static int getRearRight(double angle, double speed) {
        return getRight(angle, speed);
    }
}
