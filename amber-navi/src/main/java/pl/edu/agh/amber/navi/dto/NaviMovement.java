package pl.edu.agh.amber.navi.dto;

public final class NaviMovement {

    private final NaviPoint referencePoint;

    private final double angle, speed;

    public NaviMovement(NaviPoint referencePoint, double angle, double speed) {
        this.referencePoint = referencePoint;
        this.angle = angle;
        this.speed = speed;
    }

    @SuppressWarnings("unused")
    public NaviPoint getReferencePoint() {
        return referencePoint;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpeed() {
        return speed;
    }
}
