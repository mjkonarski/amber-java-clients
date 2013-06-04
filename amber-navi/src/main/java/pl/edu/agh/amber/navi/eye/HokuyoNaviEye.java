package pl.edu.agh.amber.navi.eye;

import gnu.io.SerialPort;
import pl.edu.agh.amber.navi.dto.NaviVisibility;
import pl.edu.agh.amber.navi.eye.hokuyo.MapPoint;
import pl.edu.agh.amber.navi.eye.hokuyo.SCIP;

import java.io.IOException;
import java.util.List;

public class HokuyoNaviEye extends NaviEyeHelper implements Runnable {

    private final SerialPort serialPort;

    private final SCIP scip;

    public HokuyoNaviEye(SerialPort serialPort) throws IOException {
        this.serialPort = serialPort;

        scip = new SCIP(serialPort.getInputStream(), serialPort.getOutputStream());
        scip.laserOn();
    }

    public void scan() {
        List<MapPoint> scan = scip.singleScan();
        if (scan != null) {
            notifyVisibilityChange(new NaviVisibility(scan));
        }
    }

    public SCIP getScip() {
        return scip;
    }

    @Override
    public void run() {
        try {
            while (true) {
                scan();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            scip.laserOff();
            e.printStackTrace();
        }
    }
}
