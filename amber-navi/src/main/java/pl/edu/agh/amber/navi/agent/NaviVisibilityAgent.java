package pl.edu.agh.amber.navi.agent;

import pl.edu.agh.amber.navi.dto.NaviVisibility;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;

import java.util.logging.Logger;

public class NaviVisibilityAgent implements Runnable {

    private static final Logger logger = Logger.getLogger(String.valueOf(NaviVisibilityAgent.class));

    // spacing between wheels in [mm] FIXME check it in really ;)
    public static final double SPACING = 250.0;

    // radius of red zone in [mm]
    public static final double RED_ZONE_RADIUS = 500.0;

    // radius of yellow zone in [mm]
    public static final double YELLOW_ZONE_RADIUS = 650.0;

    // radius of green zone inb [mm]
    public static final double GREEN_ZONE_RADIUS = 800.0;

    private final NaviEyeHelper eyeHelper;

    private final NaviDriveAgent naviDriveAgent;

    public NaviVisibilityAgent(NaviEyeHelper eyeHelper, NaviDriveAgent naviDriveAgent) {
        this.eyeHelper = eyeHelper;
        this.naviDriveAgent = naviDriveAgent;
    }

    @Override
    public void run() {
        try {
            NaviVisibility visibility = eyeHelper.getVisibility();
            if (visibility != null) {
                // paoolo TODO determine how to change trajectory
            } else {
                logger.warning("No information about visibility");
            }
            Thread.sleep(100);
        } catch (InterruptedException e) {
            logger.warning("NaviVisibilityAgent interrupted: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static double getAngle(double left, double right) {
        int measure = Double.compare(left, right);
        if (measure == 0) {
            return 0.0;
        } else if (measure < -1) {
            return Math.toDegrees(Math.tanh((right - left) / SPACING));
        } else {
            return -Math.toDegrees(Math.tanh((left - right) / SPACING));
        }
    }

    private static double getLength(double left, double right, NaviVisibility visibility) {
        double angle = getAngle(left, right);
        return visibility.getLengthForAngle(angle);
    }
}
