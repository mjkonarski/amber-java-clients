package pl.edu.agh.amber.navi.agent;

import pl.edu.agh.amber.navi.drive.NaviDriveHelper;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;
import pl.edu.agh.amber.navi.track.NaviTrackHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class NaviAgent implements Runnable {

    private static final Logger logger = Logger.getLogger(String.valueOf(NaviAgent.class));

    private final List<NaviPoint> route = Collections.synchronizedList(new LinkedList<NaviPoint>());

    private final NaviDriveHelper driveHelper;

    private final NaviTrackHelper trackHelper;

    private final NaviEyeHelper eyeHelper;

    private boolean running = true;

    public NaviAgent(NaviDriveHelper driveHelper, NaviTrackHelper trackHelper, NaviEyeHelper eyeHelper) {
        this.driveHelper = driveHelper;
        this.trackHelper = trackHelper;
        this.eyeHelper = eyeHelper;
    }

    synchronized void stop() {
        running = false;
    }

    synchronized boolean isRunning() {
        return running;
    }

    public void addLastPoint(NaviPoint point) {
        synchronized (route) {
            route.add(point);
            route.notifyAll();
        }
    }

    public void addLastPoints(List<NaviPoint> points) {
        synchronized (route) {
            route.addAll(points);
            points.notifyAll();
        }
    }

    public void addAfterPoint(NaviPoint point, NaviPoint after) {
        synchronized (route) {
            route.add(getIndex(after), point);
            route.notifyAll();
        }
    }

    public void addAfterPoints(List<NaviPoint> points, NaviPoint after) {
        synchronized (route) {
            route.addAll(getIndex(after), points);
            points.notifyAll();
        }
    }

    @Override
    public void run() {
        NaviPoint nextTarget, currentLocation;
        NaviAdjustment adjustment;
        try {
            while (isRunning()) {
                synchronized (route) {
                    while (route.isEmpty()) {
                        route.wait();
                    }
                    nextTarget = route.remove(0);
                }
                currentLocation = trackHelper.getCurrentLocation();
                while (!isTargetInTarget(currentLocation, nextTarget)) {
                    adjustment = getAdjustment(currentLocation, nextTarget);
                    try {
                        driveHelper.change(adjustment.getAngle(), adjustment.getLength());
                    } catch (IOException e) {
                        logger.warning("Cannot change drive parameters: " + e.getMessage());
                    }

                    Thread.sleep(100);
                    currentLocation = trackHelper.getCurrentLocation();
                }
            }
        } catch (InterruptedException e) {
            logger.warning("NaviAgent interrupted: " + e.getMessage());
        }
    }

    private int getIndex(NaviPoint point) {
        boolean found = false;
        int index = 0;
        Iterator<NaviPoint> itr = route.iterator();
        while (!found && itr.hasNext()) {
            if (itr.next().equals(point)) {
                found = true;
            } else {
                index += 1;
            }
        }
        return index;
    }

    private static boolean isTargetInTarget(NaviPoint currentLocation, NaviPoint target) {
        double horizontal = Math.pow(target.getAbsoluteHorizontal() - currentLocation.getAbsoluteHorizontal(), 2.0);
        double vertical = Math.pow(target.getAbsoluteVertical() - currentLocation.getAbsoluteVertical(), 2.0);
        double radius = Math.pow(target.getRadius(), 2.0);
        return (horizontal + vertical <= radius);
    }

    private static NaviAdjustment getAdjustment(NaviPoint currentLocation, NaviPoint target) {
        double horizontal = currentLocation.getAbsoluteHorizontal() - target.getAbsoluteHorizontal();
        double vertical = currentLocation.getAbsoluteVertical() - target.getAbsoluteVertical();
        double angle = Math.tanh(vertical / horizontal) - 90.0;
        double length = Math.sqrt(Math.pow(horizontal, 2.0) + Math.pow(vertical, 2.0));
        return new NaviAdjustment(currentLocation, angle, length);
    }
}
