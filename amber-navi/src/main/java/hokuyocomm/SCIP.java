package hokuyocomm;

import java.io.InputStream;
import java.io.OutputStream;
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

    private static final String OOP = "00P";

    private static final String LASER_ON = "BM";

    private static final String LASER_OFF = "QT";

    private static final String RESET = "RS";

    private static final String MOTOR_SPEED = "CR";

    private static final String HIGH_SENSITIVE = "HS";

    private static final String VERSION_INFO = "VV";

    private static final String SENSOR_SPECS = "PP";

    private static final String SENSOR_STATE = "II";

    private static final String SINGLE_SCAN = "GD00440725";

    private static final String MULTI_SCAN = "MD00440725";

    private final Scanner sc;

    private final PrintStream ps;

    public SCIP(InputStream in, OutputStream out) {
        sc = new Scanner(in);
        ps = new PrintStream(out);
    }

    private String get(String code, int count) {
        ps.println(code);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String str = sc.nextLine();
            result.append(str.substring(0, str.length() - 2));
        }
        sc.nextLine();

        return result.toString();
    }

    /**
     * Command "BM", page 14
     */
    public String laserOn() {
        return get(LASER_ON, 3);
    }

    /**
     * Command "QT", page 14
     */
    public String laserOff() {
        return get(LASER_OFF, 3);
    }

    /**
     * Command "RS", page 14
     */
    public String reset() {
        return get(RESET, 3);
    }

    public static enum MotorSpeed {
        DEFAULT {
            @Override
            public String getValue() {
                return "00";
            }
        }, LEVEL_1 {
            @Override
            public String getValue() {
                return "01";
            }
        }, LEVEL_2 {
            @Override
            public String getValue() {
                return "02";
            }
        }, LEVEL_3 {
            @Override
            public String getValue() {
                return "03";
            }
        }, LEVEL_4 {
            @Override
            public String getValue() {
                return "04";
            }
        }, LEVEL_5 {
            @Override
            public String getValue() {
                return "05";
            }
        }, LEVEL_6 {
            @Override
            public String getValue() {
                return "06";
            }
        }, LEVEL_7 {
            @Override
            public String getValue() {
                return "07";
            }
        }, LEVEL_8 {
            @Override
            public String getValue() {
                return "08";
            }
        }, LEVEL_9 {
            @Override
            public String getValue() {
                return "09";
            }
        }, LEVEL_10 {
            @Override
            public String getValue() {
                return "10";
            }
        }, RESET {
            @Override
            public String getValue() {
                return "99";
            }
        };

        public abstract String getValue();
    }

    /**
     * Command "CR", page 17
     */
    public String setMotorSpeed(MotorSpeed motorSpeed) {
        return get(MOTOR_SPEED + motorSpeed.getValue(), 3);
    }

    /**
     * Command "HS", page 18
     */
    public String setHighSensitive(boolean enable) {
        return get(HIGH_SENSITIVE + (enable ? "0" : "1"), 3);
    }

    /**
     * Command "VV", page 20
     */
    public String getVersionInfo() {
        return get(VERSION_INFO, 8);
    }

    /**
     * Command "PP", page 21
     */
    public String getSensorSpecs() {
        return get(SENSOR_SPECS, 11);
    }

    /**
     * Command "II", page 22
     */
    public String getSensorState() {
        return get(SENSOR_STATE, 10);
    }

    private List<MapPoint> fetchData() {
        List<MapPoint> points = new LinkedList<MapPoint>();

        // parse timestamp
        int timeStamp = SCIP.decode(sc.nextLine().substring(0, 4));

        // fetch data
        int numDataBlocks = 32;
        List<String> measurements = new LinkedList<String>();
        for (int i = 0; i < numDataBlocks; i++) {
            String receivedLine = sc.nextLine();
            measurements.add(receivedLine.substring(0, receivedLine.length() - 1));
        }

        // 725 (end of measurement) - 44 (end of measurement) = 681 values
        int i = 0;
        for (String measurement : measurements) {
            double distanceValue = (double) SCIP.decode(measurement.substring(0, 3));
            points.add(new MapPoint(distanceValue, (0.3515625 * i) - 29.53125, timeStamp));
            i += 1;
        }

        // return list of points
        return points;
    }

    /**
     * Command "GD", page 13
     */
    public List<MapPoint> singleScan(int clusterCount) {
        List<MapPoint> points = new LinkedList<MapPoint>();

        // send command
        ps.println(SINGLE_SCAN + intToString(clusterCount, 2));

        // drop first line
        sc.nextLine();

        String receivedLine = sc.nextLine();
        // check if line is valid
        if (receivedLine.substring(0, 3).contains(OOP)) {
            // fetch data
            return fetchData();

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
                case 10:
                    logger.warning("Laser is turned off.");
                    break;
                default:
                    logger.warning("Hardware issue #" + status + ".");
                    break;
            }
            throw new RuntimeException();
        }
    }

    /**
     * Command "MD", page 11
     */
    public List<List<MapPoint>> multiScan(int numScans, int scanInterval, int clusterCount) {
        List<List<MapPoint>> points = new LinkedList<List<MapPoint>>();

        ps.println(MULTI_SCAN + intToString(clusterCount, 2) + intToString(scanInterval, 0) + intToString(numScans, 2));

        // drop first line
        sc.nextLine();

        String receivedLine = sc.nextLine();
        // check if line is valid
        if (receivedLine.contains(OOP)) {
            int remainingScans = numScans;
            while (remainingScans > 0) {
                // drop line
                sc.nextLine();

                // get information about remaining scans
                remainingScans = Integer.parseInt(sc.nextLine().substring(14));
                if (sc.nextLine().contains("99b")) {
                    // fetch data
                    points.add(fetchData());
                }
            }

            // return list of points
            return points;

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
            throw new RuntimeException();
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
