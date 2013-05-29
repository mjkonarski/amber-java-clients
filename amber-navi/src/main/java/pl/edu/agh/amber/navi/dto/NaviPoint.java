package pl.edu.agh.amber.navi.dto;

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

    public NaviPoint(double horizontal, double vertical) {
        this(null, horizontal, vertical, 0.0);
    }

    @SuppressWarnings("unused")
    public double getHorizontal() {
        return horizontal;
    }

    public double getAbsoluteHorizontal() {
        return this.horizontal + (referenceCenter != null ? referenceCenter.getAbsoluteHorizontal() : 0.0);
    }

    @SuppressWarnings("unused")
    public double getVertical() {
        return vertical;
    }

    public double getAbsoluteVertical() {
        return this.vertical + (referenceCenter != null ? referenceCenter.getAbsoluteVertical() : 0.0);
    }

    public double getRadius() {
        return radius;
    }

    @SuppressWarnings("unused")
    public NaviPoint getAbsolutePoint() {
        return new NaviPoint(this.horizontal + (referenceCenter != null ? referenceCenter.getAbsoluteHorizontal() : 0.0),
                this.vertical + (referenceCenter != null ? referenceCenter.getAbsoluteVertical() : 0.0), this.radius);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NaviPoint) {
            NaviPoint other = (NaviPoint) o;
            double horizontalSquare = Math.pow(other.getAbsoluteHorizontal() - this.getAbsoluteHorizontal(), 2.0);
            double verticalSquare = Math.pow(other.getAbsoluteVertical() - this.getAbsoluteVertical(), 2.0);
            double radiusSquare = Math.pow(Math.max(other.getRadius(), this.getRadius()), 2.0);
            return (horizontalSquare + verticalSquare <= radiusSquare);
        }
        return false;
    }

    public NaviMovement getDifference(NaviPoint target, int time) {
        double horizontal = getAbsoluteHorizontal() - target.getAbsoluteHorizontal();
        double vertical = getAbsoluteVertical() - target.getAbsoluteVertical();
        int angle = (int) Math.toDegrees(Math.tanh(vertical / horizontal)) - 90;
        int distance = (int) Math.sqrt(Math.pow(horizontal, 2.0) + Math.pow(vertical, 2.0));
        return new NaviMovement(this, angle, distance, time);
    }
}
