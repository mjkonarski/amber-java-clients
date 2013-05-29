package pl.edu.agh.amber.navi.eye;

import gnu.io.SerialPort;
import hokuyocomm.SCIP;
import pl.edu.agh.amber.navi.dto.NaviVisibility;

import java.io.IOException;

public class HokuyoNaviEye extends NaviEyeHelper {

    private final SerialPort serialPort;

    private final SCIP scip;

    public HokuyoNaviEye(SerialPort serialPort) throws IOException {
        this.serialPort = serialPort;

        scip = new SCIP(serialPort.getInputStream(), serialPort.getOutputStream());
    }

    public void scan(int clusterScan) {
        notifyVisibilityChange(new NaviVisibility(scip.singleScan(clusterScan)));
    }
}
