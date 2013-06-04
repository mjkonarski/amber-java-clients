package pl.edu.agh.amber.navi.app.test;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import pl.edu.agh.amber.navi.NaviConfig;
import pl.edu.agh.amber.navi.tool.SerialPortHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class NaviStarGazerTrackTest {

    public static void main(String[] args) throws PortInUseException, UnsupportedCommOperationException,
            NoSuchPortException, IOException {

        String starGazerPortName = NaviConfig.getStarGazerPortName();
        SerialPort starGazerSerialPort = SerialPortHelper.getStarGazerSerialPort(starGazerPortName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(starGazerSerialPort.getInputStream()));
        PrintStream ps = new PrintStream(starGazerSerialPort.getOutputStream());

        ps.println("#CalcStart");
        String string;
        while ((string = reader.readLine()) != null) {
            System.err.println(string);
        }
        ps.println("#CalcStop");
    }
}
