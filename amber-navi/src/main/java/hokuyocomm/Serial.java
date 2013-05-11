package hokuyocomm;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;

/**
 * @author Nick Schulz
 */
public class Serial {

    private static SerialPort s;

    private static InputStream in;

    private static PrintStream out;

    public static String getComPortName() {
        return s.getName();
    }

    public static void getAvailablePorts() {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier com;

        System.out.print("Available ports: ");

        while (ports.hasMoreElements()) {
            com = (CommPortIdentifier) ports.nextElement();
            if (!com.isCurrentlyOwned()) {
                System.out.print(com.getName() + ", ");
            }
        }
        System.out.println();
    }

    public static void open(String comPort, int baudRate, int dataBits, int stopBits, int parity, int millis) {
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier com;

        comPort = comPort.toUpperCase();

        do {
            com = (CommPortIdentifier) ports.nextElement();
        } while (ports.hasMoreElements() && !comPort.equals(com.getName()));

        if (comPort.equals(com.getName())) {
            try {
                s = (SerialPort) com.open("HokuyoSerial", 2000);
                s.setSerialPortParams(baudRate, dataBits, stopBits, parity);
                Thread.sleep(millis);

                in = s.getInputStream();
                OutputStream serOut;
                serOut = s.getOutputStream();
                out = new PrintStream(serOut);

                System.out.println("Opened serial connection on port " + s.getName() + ".");
            } catch (PortInUseException e) {
                System.err.println("Error: Port " + com.getName() + " already in use by " + com.getCurrentOwner() + ".");
            } catch (UnsupportedCommOperationException e) {
                System.err.println("Error: " + e.getMessage() + ".");
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage() + ".");
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage() + ".");
            }


        } else {
            System.err.println("Error: The specified port " + com.getName() + " does not exist.");
        }
    }

    public static void close() {
        try {
            s.close();
            in.close();
            out.close();

            System.out.println("Closed serial connection on port " + s.getName() + ".");
        } catch (NullPointerException e) {
            System.err.println("Error: There is no open serial connection to close.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage() + ".");
        }
    }
}
