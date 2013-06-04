package pl.edu.agh.amber.navi.tool;

import gnu.io.*;

import java.io.IOException;
import java.util.logging.Logger;

public class SerialPortHelper {

    private static final Logger logger = Logger.getLogger(String.valueOf(SerialPortHelper.class));

    public static SerialPort getSerialPort(String portName, String owner, int baudRate, int dataBits,
                                           int stopBits, int parity, int magicNumber)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {

        CommPortIdentifier id = CommPortIdentifier.getPortIdentifier(portName);

        if (id.getPortType() != CommPortIdentifier.PORT_SERIAL) {
            throw new NoSuchPortException();

        } else {
            SerialPort sp = (SerialPort) id.open(owner, magicNumber);
            sp.setSerialPortParams(baudRate, dataBits, stopBits, parity);
            return sp;
        }
    }

    public static SerialPort getHoluxSerialPort(String portName)
            throws PortInUseException, IOException, NoSuchPortException, UnsupportedCommOperationException {

        return getSerialPort(portName, "holux", 4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, 1000);
    }

    public static SerialPort getHokuyoSerialPort(String portName)
            throws PortInUseException, IOException, NoSuchPortException, UnsupportedCommOperationException {

        return getSerialPort(portName, "hokuyo", 9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, 1001);
    }

    public static SerialPort getStarGazerSerialPort(String portName)
            throws PortInUseException, IOException, NoSuchPortException, UnsupportedCommOperationException {

        return getSerialPort(portName, "star_gazer", 115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, 1002);
    }
}
