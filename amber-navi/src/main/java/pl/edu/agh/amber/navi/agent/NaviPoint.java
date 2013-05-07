package pl.edu.agh.amber.navi.agent;

public final class NaviPoint {

    private final NaviPoint referenceCenter;

    private final double horizontal, vertical, radius;

    public NaviPoint(NaviPoint referenceCenter, double horizontal, double vertical, double radius) {
        this.referenceCenter = referenceCenter;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.radius = radius;
    }

    public NaviPoint(double horizontal, double vertical, double radius) {
        this(null, horizontal, vertical, radius);
    }

    public double getHorizontal() {
        return horizontal;
    }

    public double getAbsoluteHorizontal() {
        return this.horizontal + (referenceCenter != null ? referenceCenter.getAbsoluteHorizontal() : 0.0);
    }

    public double getVertical() {
        return vertical;
    }

    public double getAbsoluteVertical() {
        return this.vertical + (referenceCenter != null ? referenceCenter.getAbsoluteVertical() : 0.0);
    }

    public double getRadius() {
        return radius;
    }

    public NaviPoint getAbsolutePoint() {
        return new NaviPoint(this.horizontal + (referenceCenter != null ? referenceCenter.getAbsoluteHorizontal() : 0.0),
                this.vertical + (referenceCenter != null ? referenceCenter.getAbsoluteVertical() : 0.0), this.radius);
    }
}
