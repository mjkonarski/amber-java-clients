package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.navi.agent.NaviAdjustment;
import pl.edu.agh.amber.roboclaw.MotorsCurrentSpeed;
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
    public void change(double changeAngle, double changeSpeed) throws IOException {
        this.currentAngle += changeAngle;
        this.currentSpeed += changeSpeed;

        int frontLeft = 0, frontRight = 0, rearLeft = 0, rearRight = 0;

        MotorsCurrentSpeed mcs = roboclawProxy.getCurrentMotorsSpeed();

        roboclawProxy.sendMotorsCommand(frontLeft, frontRight, rearLeft, rearRight);
    }

    @Override
    public void change(NaviAdjustment adjustment) throws IOException {
        // paoolo FIXME it will detect speed to adjust
        change(adjustment.getAngle(), adjustment.getLength());
    }

}
