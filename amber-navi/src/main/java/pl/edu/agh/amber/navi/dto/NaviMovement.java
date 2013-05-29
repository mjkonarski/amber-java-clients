package pl.edu.agh.amber.navi.dto;

public final class NaviMovement {

    private NaviPoint referencePoint;

    private int angle, length, time;

    public NaviMovement(NaviPoint referencePoint, int angle, int length, int time) {
        this.referencePoint = referencePoint;
        this.angle = angle;
        this.length = length;
        this.time = time;
    }

    @SuppressWarnings("unused")
    public NaviPoint getReferencePoint() {
        return referencePoint;
    }

    public void setReferencePoint(NaviPoint referencePoint) {
        this.referencePoint = referencePoint;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
