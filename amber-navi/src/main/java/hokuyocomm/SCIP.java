package hokuyocomm;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author Nick Schulz
 */
public class SCIP {

    private static final Logger logger = Logger.getLogger(String.valueOf(SCIP.class));

    private static final String VERSION_INFO = "VV";

    private static final String SENSOR_SPECS = "PP";

    private static final String SENSOR_STATE = "II";

    private static final String LASER_ON = "BM";

    private static final String LASER_OFF = "QT;LaserShutOff";

    private static final String RESET = "RS";

    private static final String SENSITIVITY = "HS";

    private static final String MOTOR_SPEED = "CR";

    private static final String SINGLE_SCAN = "GD00440725";

    private static final String MULTI_SCAN = "MD00440725";

    private static final String OOP = "00P";

    private final List queue = new LinkedList();

    private InputStream in;

    private PrintStream out;

    private String get(String code, int count) {
        out.println(code);
        Scanner sc = new Scanner(in);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String str = sc.nextLine();
            result.append(str.substring(0, str.length() - 2));
        }
        sc.nextLine();

        return result.toString();
    }

    public String getVersionInfo() {
        return get(VERSION_INFO, 7);
    }

    public String getSensorSpecs() {
        return get(SENSOR_SPECS, 10);
    }

    public String getSensorState() {
        return get(SENSOR_STATE, 9);
    }

    public void reset() {
        out.println(RESET);
    }

    public void laserOff() {
        out.println(LASER_OFF);
    }

    public void laserOn() {
        out.println(LASER_ON);
        Scanner sc = new Scanner(in);

        sc.nextLine();
        switch (Integer.parseInt(sc.nextLine().substring(0, 2))) {
            case 1:
                logger.warning("A malfunction is preventing the laser from turning on.");
                break;
            case 2:
                logger.info("Command ignored. Laser is already powered on.");
                break;
        }
        sc.nextLine();
    }

    public void setSensitivity(int mode) {
        // paoolo FIXME add leading zero?
        out.println(SENSITIVITY + intToString(mode, 0));
        Scanner sc = new Scanner(in);

        sc.nextLine();
        switch (Integer.parseInt(sc.nextLine().substring(0, 2))) {
            case 1:
                logger.warning("Parameter Error.");
                break;
            case 2:
                logger.info("Command ignored. Already running on desired sensitivity mode.");
                break;
            case 3:
                logger.warning("Incompatible with current sensor model.");
                break;
        }
        sc.nextLine();
    }

    public void setMotorSpeed(int motorSpeed) {
        out.println(MOTOR_SPEED + intToString(motorSpeed, 2));
        Scanner sc = new Scanner(in);

        sc.nextLine();
        switch (Integer.parseInt(sc.nextLine().substring(0, 2))) {
            case 1:
                logger.warning("Invalid speed ratio.");
                break;
            case 2:
                logger.warning("Speed ratio out of range.");
                break;
            case 3:
                logger.info("Command ignored. Motor is already running at desired speed.");
                break;
            case 4:
                logger.warning("Incompatible with current sensor model.");
                break;
        }
        sc.nextLine();
    }

    public boolean singleScan(int clusterCount) {
        out.println(SINGLE_SCAN + intToString(clusterCount, 2));
        Scanner sc = new Scanner(in);

        sc.nextLine();
        String receivedLine = sc.nextLine();
        if (receivedLine.substring(0, 3).contains(OOP)) {
            int timeStamp = SCIP.decode(sc.nextLine().substring(0, 4));
            int numDataBlocks = 32;
            StringBuilder encDataStr = new StringBuilder();

            for (int i = 0; i < numDataBlocks; i++) {
                receivedLine = sc.nextLine();
                encDataStr.append(receivedLine.substring(0, receivedLine.length() - 1));
            }

            for (int i = 0; i < 682; i++) {
                short distanceValue = (short) SCIP.decode(encDataStr.substring(i * 3, (i * 3) + 3));

                if (distanceValue > 0) {
                    MapPoint laserData = new MapPoint(distanceValue, (0.3515625 * i) - 29.53125, timeStamp);
                    boolean add = queue.add(laserData);
                }
            }
            return true;
        } else {
            switch (Integer.parseInt(receivedLine.substring(0, 2))) {
                case 1:
                    logger.warning("Start step has non-numeric value.");
                    break;
                case 2:
                    logger.warning("End step has non-numeric value.");
                    break;
                case 3:
                    logger.warning("Cluster count has non-numeric value.");
                    break;
                case 4:
                    logger.warning("End step is out of range.");
                    break;
                case 5:
                    logger.warning("End step is smaller than start step.");
                    break;
                case 10:
                    logger.warning("Laser is turned off.");
                    break;
                default:
                    logger.warning("Hardware issue");
                    break;
            }
            return false;
        }
    }

    public void multiScan(int numScans, int scanInterval, int clusterCount) {
        out.println(MULTI_SCAN + intToString(clusterCount, 2) + intToString(scanInterval, 0) + intToString(numScans, 2));
        Scanner sc = new Scanner(in);

        sc.nextLine();
        String receivedLine = sc.nextLine();
        if (receivedLine.contains(OOP)) {
            int remainingScans = numScans;
            while (remainingScans > 0) {
                sc.nextLine();
                remainingScans = Integer.parseInt(sc.nextLine().substring(14));
                if (sc.nextLine().contains("99b")) {
                    int timeStamp = SCIP.decode(sc.nextLine().substring(0, 4));
                    int numDataBlocks = 32;
                    String encDataStr = "";

                    for (int i = 0; i < numDataBlocks; i++) {
                        receivedLine = sc.nextLine();
                        encDataStr += receivedLine.substring(0, receivedLine.length() - 1);
                    }
                    for (int i = 0; i < 682; i++) {
                        short distanceValue = (short) SCIP.decode(encDataStr.substring(i * 3, (i * 3) + 3));

                        if (distanceValue > 0) {
                            MapPoint laserData = new MapPoint(distanceValue, (0.3515625 * i) - 29.53125, timeStamp);
                            boolean add = queue.add(laserData);
                        }
                    }
                }
            }
        } else {
            int status = Integer.parseInt(receivedLine.substring(0, 2));

            switch (status) {
                case 1:
                    logger.warning("Start step has non-numeric value.");
                    break;
                case 2:
                    logger.warning("End step has non-numeric value.");
                    break;
                case 3:
                    logger.warning("Cluster count has non-numeric value.");
                    break;
                case 4:
                    logger.warning("End step is out of range.");
                    break;
                case 5:
                    logger.warning("End step is smaller than start step.");
                    break;
                case 6:
                    logger.warning("Scan interval has non-numeric value.");
                    break;
                case 7:
                    logger.warning("Number of scans has non-numeric value.");
                    break;
                default:
                    logger.warning("Hardware issue #" + status + ".");
                    break;
            }
        }
    }

    private static int decode(String enc) {
        char[] encArray = enc.toCharArray();
        String binaryDec = "";

        for (int i = 0; i < encArray.length; i++) {
            encArray[i] -= 0x30;
            String str = Integer.toBinaryString(encArray[i]);

            while (str.length() < 6) {
                str = "0" + str;
            }
            binaryDec += str;
        }
        return Integer.parseInt(binaryDec, 2);
    }

    private static String intToString(int value, int count) {
        StringBuilder string = new StringBuilder(Integer.toString(value));
        while (string.length() < count) {
            string.insert(0, "0");
        }
        return string.toString();
    }
}
