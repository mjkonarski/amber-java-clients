package pl.edu.agh.amber.navi.app.test;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import pl.edu.agh.amber.navi.NaviConfig;
import pl.edu.agh.amber.navi.eye.HokuyoNaviEye;
import pl.edu.agh.amber.navi.eye.hokuyo.SCIP;
import pl.edu.agh.amber.navi.tool.SerialPortHelper;

import java.io.IOException;

public class NaviHokuyoEyeTest {

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException,
            NoSuchPortException, IOException {

        String hokuyoPortName = NaviConfig.getHokuyoPortName();
        SerialPort hokuyoSerialPort = SerialPortHelper.getHokuyoSerialPort(hokuyoPortName);
        HokuyoNaviEye eyeHelper = new HokuyoNaviEye(hokuyoSerialPort);

        SCIP scip = eyeHelper.getScip();

        scip.laserOff();
        scip.laserOn();
        scip.setHighSensitive(true);
        scip.setMotorSpeed(SCIP.MotorSpeed.DEFAULT);

        System.err.println(scip.getVersionInfo());
        System.err.println(scip.getSensorState());
        System.err.println(scip.getSensorSpecs());

        System.err.println(scip.singleScan());

        scip.laserOff();
    }
}
