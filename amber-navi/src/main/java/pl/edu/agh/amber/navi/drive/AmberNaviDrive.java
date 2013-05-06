package pl.edu.agh.amber.navi.drive;

import pl.edu.agh.amber.common.CyclicDataListener;
import pl.edu.agh.amber.ninedof.NinedofData;
import pl.edu.agh.amber.ninedof.NinedofProxy;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;

import java.io.IOException;
import java.util.logging.Logger;

public class AmberNaviDrive extends NaviDriveHelper {

    private static final Logger logger = Logger.getLogger(String.valueOf(AmberNaviDrive.class));

    private final NinedofProxy ninedofProxy;

    private final RoboclawProxy roboclawProxy;

    private double currentAngle, currentSpeed;

    public AmberNaviDrive(NinedofProxy ninedofProxy, RoboclawProxy roboclawProxy) throws IOException {
        this.ninedofProxy = ninedofProxy;
        this.roboclawProxy = roboclawProxy;

        ninedofProxy.registerNinedofDataListener(10, true, true, true, new CyclicDataListener<NinedofData>() {
            @Override
            public void handle(NinedofData data) {
                try {
                    logger.info(String.format("accel: %d %d %d, gyro: %d %d %d, magnet: %d %d %d",
                            data.getAccel().xAxis, data.getAccel().yAxis, data.getAccel().zAxis,
                            data.getGyro().xAxis, data.getGyro().yAxis, data.getGyro().zAxis,
                            data.getMagnet().xAxis, data.getMagnet().yAxis, data.getMagnet().zAxis));
                } catch (Exception e) {
                    logger.warning("Exception occurred: " + e);
                }
            }
        });
    }

    public double getCurrentAngle() {
        return currentAngle;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    public void change(double changeAngle, double changeSpeed) {
        this.currentAngle += changeAngle;
        this.currentSpeed += changeSpeed;
    }

}
