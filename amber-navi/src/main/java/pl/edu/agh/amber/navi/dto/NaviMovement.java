package pl.edu.agh.amber.navi.dto;

public final class NaviMovement {

    private NaviPoint referencePoint;

    private double angle, length;

    public NaviMovement(NaviPoint referencePoint, double angle, double length) {
        this.referencePoint = referencePoint;
        this.angle = angle;
        this.length = length;
    }

    public NaviPoint getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(NaviPoint referencePoint) {
        this.referencePoint = referencePoint;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }
}
