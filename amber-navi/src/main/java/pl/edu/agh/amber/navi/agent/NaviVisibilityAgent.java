package pl.edu.agh.amber.navi.agent;

import pl.edu.agh.amber.navi.NaviConst;
import pl.edu.agh.amber.navi.dto.NaviVisibility;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class NaviVisibilityAgent implements Runnable {

    private static final Logger logger = Logger.getLogger(String.valueOf(NaviVisibilityAgent.class));

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
                List<Double> zone = visibility.getAnglesForLength(NaviConst.RED_ZONE_RADIUS);
                if (checkZone(-10.0, 10.0, zone)) {
                    zone = visibility.getAnglesForLength(NaviConst.YELLOW_ZONE_RADIUS);
                    if (checkZone(-25.0, 25.0, zone)) {
                        // paoolo TODO sth here
                    } else {
                        // paoolo TODO sth here
                    }
                }
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
            return Math.toDegrees(Math.tanh((right - left) / NaviConst.SPACING));
        } else {
            return -Math.toDegrees(Math.tanh((left - right) / NaviConst.SPACING));
        }
    }

    private static double getLength(double left, double right, NaviVisibility visibility) {
        double angle = getAngle(left, right);
        return visibility.getLengthForAngle(angle);
    }

    private static boolean checkZone(double leftAngle, double rightAngle, List<Double> angles) {
        boolean found = false;
        Iterator<Double> iterator = angles.iterator();
        while (!found && iterator.hasNext()) {
            double val = iterator.next();
            found = Double.compare(leftAngle, val) < 0 && Double.compare(val, rightAngle) < 0;
        }
        return found;
    }
}
