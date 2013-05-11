package hokuyocomm;

/**
 * @author GreenWing
 */
public class MapPoint {

    private short distance;

    private double angle;

    private int timeStamp;

    public MapPoint(short d, double a, int t) {
        distance = d;
        angle = a;
        timeStamp = t;
    }

    public short getDistance() {
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
