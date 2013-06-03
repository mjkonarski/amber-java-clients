package pl.edu.agh.amber.navi.app;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import pl.edu.agh.amber.navi.NaviConfig;
import pl.edu.agh.amber.navi.tool.SerialPortHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NaviTrackMain {

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException, NoSuchPortException,
            IOException, InterruptedException {

        String holuxPortName = NaviConfig.getHoluxPortName();
        SerialPort holuxSerialPort = SerialPortHelper.getHoluxSerialPort(holuxPortName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(holuxSerialPort.getInputStream()));
        while (true) {
            System.err.println(reader.readLine());
        }
    }
}
