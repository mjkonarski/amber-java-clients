package pl.edu.agh.amber.navi.tool;

import gnu.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class SerialPortHelper {

    private static final Logger logger = Logger.getLogger(String.valueOf(SerialPortHelper.class));

    /**
     * Scan serial ports for NMEA data.
     *
     * @return SerialPort from which NMEA data was found, or null if data was
     *         not found in any of the ports.
     */
    public static SerialPortWrapper getSerialPort(String portName, String owner, int magicNumber) throws NoSuchPortException,
            PortInUseException, UnsupportedCommOperationException, IOException {

        CommPortIdentifier id = CommPortIdentifier.getPortIdentifier(portName);

        if (id.getPortType() != CommPortIdentifier.PORT_SERIAL) {
            throw new NoSuchPortException();

        } else {
            SerialPort sp = (SerialPort) id.open(owner, magicNumber);
            sp.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

            InputStream is = sp.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader buf = new BufferedReader(isr);

            return new SerialPortWrapper(sp, is, isr, buf);
        }
    }
}
