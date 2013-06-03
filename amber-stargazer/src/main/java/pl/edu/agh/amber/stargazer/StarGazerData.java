package pl.edu.agh.amber.stargazer;

import pl.edu.agh.amber.common.FutureObject;

public class StarGazerData extends FutureObject {
    private int XAxis;
    private int YAxis;
    private int angle;

    public void setXAxis(int XAxis) {
        this.XAxis = XAxis;
    }

    public int getXAxis() {
        return XAxis;
    }

    public void setYAxis(int YAxis) {
        this.YAxis = YAxis;
    }

    public int getYAxis() {
        return YAxis;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }
}
