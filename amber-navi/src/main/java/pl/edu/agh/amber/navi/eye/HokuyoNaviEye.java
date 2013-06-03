package pl.edu.agh.amber.navi.eye;

import gnu.io.SerialPort;
import hokuyocomm.MapPoint;
import hokuyocomm.SCIP;
import pl.edu.agh.amber.navi.dto.NaviVisibility;

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

    public void scan(int clusterScan) {
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
                scan(0);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            scip.laserOff();
            e.printStackTrace();
        }
    }
}
