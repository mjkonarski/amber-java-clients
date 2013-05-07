package pl.edu.agh.amber.navi.eye;

import pl.edu.agh.amber.navi.tool.SerialPortWrapper;

public class HokuyoNaviEye extends NaviEyeHelper {

    private final SerialPortWrapper serialPortWrapper;

    public HokuyoNaviEye(SerialPortWrapper serialPortWrapper) {
        this.serialPortWrapper = serialPortWrapper;
    }
}
