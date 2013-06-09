package pl.edu.agh.amber.navi.app;

import gnu.io.SerialPort;
import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.navi.NaviConfig;
import pl.edu.agh.amber.navi.agent.NaviDriveAgent;
import pl.edu.agh.amber.navi.agent.NaviTrackAgent;
import pl.edu.agh.amber.navi.agent.NaviVisibilityAgent;
import pl.edu.agh.amber.navi.drive.AmberNaviDrive;
import pl.edu.agh.amber.navi.drive.NaviDriveHelper;
import pl.edu.agh.amber.navi.dto.NaviPoint;
import pl.edu.agh.amber.navi.eye.HokuyoNaviEye;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;
import pl.edu.agh.amber.navi.tool.ConsoleHelper;
import pl.edu.agh.amber.navi.tool.KeyListener;
import pl.edu.agh.amber.navi.tool.SerialPortHelper;
import pl.edu.agh.amber.navi.track.HoluxNaviTrack;
import pl.edu.agh.amber.navi.track.NaviTrackHelper;
import pl.edu.agh.amber.navi.track.StarGazerNaviTrack;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Contains heart of automated agent, to driving robo, using information
 * from tracking system and digital eye.
 */
public class NaviMain {

    private static final Logger logger = Logger.getLogger(String.valueOf(NaviMain.class));

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    private static List<NaviPoint> parseTrack(String filename, NaviPoint referenceCenter) throws IOException {
        List<NaviPoint> route = new LinkedList<NaviPoint>();
        File file = new File(filename);
        int lineNumber = 0;
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        while ((line = reader.readLine()) != null) {
            lineNumber += 1;
            String[] values = line.trim().replaceAll("\\s+", " ").split("\\s");
            if (values.length < 3) {
                logger.warning(String.format("Line %d has not enough information. Line omitted.", lineNumber));
            } else {
                route.add(new NaviPoint(referenceCenter, parseInt(values[0]), parseInt(values[1]), parseInt(values[2])));
            }
        }
        return route;
    }

    public static void main(String[] args) throws Exception {

        NaviPoint referenceCenter = new NaviPoint(NaviConfig.getRefCenterHorizontal(),
                NaviConfig.getRefCenterVertical(), 0.0);

        String holuxPortName = NaviConfig.getHoluxPortName();
        String hokuyoPortName = NaviConfig.getHokuyoPortName();
        String starGazerPortName = NaviConfig.getStarGazerPortName();

        String hostname = NaviConfig.getHostname();
        int port = NaviConfig.getPort();
        List<NaviPoint> route = parseTrack(NaviConfig.getTrack(), referenceCenter);

        AmberClient amberClient = new AmberClient(hostname, port);
        RoboclawProxy roboclawProxy = new RoboclawProxy(amberClient, 0);

        NaviDriveHelper driveHelper;
        NaviTrackHelper trackHelper;
        NaviEyeHelper eyeHelper;

        switch (NaviConfig.getTrackHelperType()) {
            case HOLUX:
                SerialPort holuxSerialPort = SerialPortHelper.getHoluxSerialPort(holuxPortName);
                trackHelper = new HoluxNaviTrack(holuxSerialPort);
                logger.info("Started Holux");
                break;
            case STAR_GAZER:
                SerialPort starGazerSerialPort = SerialPortHelper.getStarGazerSerialPort(starGazerPortName);
                trackHelper = new StarGazerNaviTrack(starGazerSerialPort);
                logger.info("Started StarGazer");
                break;
            default:
                trackHelper = null;
                logger.warning("No location device.");
        }

        switch (NaviConfig.getEyeHelperType()) {
            case HOKUYO:
                SerialPort hokuyoSerialPort = SerialPortHelper.getHokuyoSerialPort(hokuyoPortName);
                eyeHelper = new HokuyoNaviEye(hokuyoSerialPort);
                Thread eyeThread = new Thread(eyeHelper);
                eyeThread.start();
                logger.info("Started Hokuyo");
                break;
            default:
                eyeHelper = null;
                logger.warning("No eye device.");
        }

        driveHelper = new AmberNaviDrive(roboclawProxy);

        final NaviDriveAgent naviDriveAgent = new NaviDriveAgent(driveHelper);
        new Thread(naviDriveAgent).start();
        logger.info("Started driver agent");

        if (eyeHelper != null) {
            NaviVisibilityAgent naviVisibilityAgent = new NaviVisibilityAgent(eyeHelper, naviDriveAgent);
            new Thread(naviVisibilityAgent).start();
            logger.info("Started visibility agent");
        }

        if (trackHelper != null) {
            NaviTrackAgent naviTrackAgent = new NaviTrackAgent(trackHelper, naviDriveAgent);
            naviTrackAgent.addLastPoints(route);
            new Thread(naviTrackAgent).start();
            logger.info("Started tracker agent");
        }

        KeyListener listener;
        listener = new KeyListener('a') {
            @Override
            public void keyPressed() {
                System.err.println("Speed up");
                naviDriveAgent.speedUp();
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('z') {
            @Override
            public void keyPressed() {
                System.err.println("Speed down");
                naviDriveAgent.speedDown();
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('j') {
            @Override
            public void keyPressed() {
                System.err.println("Turn left");
                naviDriveAgent.turnLeft();
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('k') {
            @Override
            public void keyPressed() {
                System.err.println("Turn right");
                naviDriveAgent.turnRight();
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('n') {
            @Override
            public void keyPressed() {
                System.err.println("Rotate left");
                naviDriveAgent.rotateLeft();
            }
        };
        ConsoleHelper.addListener(listener);
        listener = new KeyListener('m') {
            @Override
            public void keyPressed() {
                System.err.println("Rotate right");
                naviDriveAgent.rotateRight();
            }
        };
        ConsoleHelper.addListener(listener);

        System.err.println("Enabling live steering");
        ConsoleHelper.main();

    }
}
