package pl.edu.agh.amber.navi.eye.hokuyo;

/**
 * @author GreenWing
 * @author Pawel Suder
 */
public class MapPoint {

    private double angle, distance;

    private int timeStamp;

    public MapPoint(double d, double a, int t) {
        distance = d;
        angle = a;
        timeStamp = t;
    }

    public double getDistance() {
        return distance;
    }

    public double getAngle() {
        return angle;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public double xValue() {
        return distance * Math.cos(Math.toRadians(angle));
    }

    public double yValue() {
        return distance * Math.sin(Math.toRadians(angle));
    }

    @Override
    public String toString() {
        return "Distance: " + distance + ", angle: " + angle + ", timestamp: " + timeStamp + "\n";
    }
}
