package pl.edu.agh.amber.navi.agent;

public final class NaviAdjustment {

    private final NaviPoint referencePoint;

    private final double angle, length;

    public NaviAdjustment(NaviPoint referencePoint, double angle, double length) {
        this.referencePoint = referencePoint;
        this.angle = angle;
        this.length = length;
    }

    public NaviPoint getReferencePoint() {
        return referencePoint;
    }

    public double getAngle() {
        return angle;
    }

    public double getLength() {
        return length;
    }
}
