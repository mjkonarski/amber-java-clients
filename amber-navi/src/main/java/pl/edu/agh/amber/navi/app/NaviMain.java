package pl.edu.agh.amber.navi.app;

import gnu.io.SerialPort;
import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.navi.NaviConfig;
import pl.edu.agh.amber.navi.agent.NaviAgent;
import pl.edu.agh.amber.navi.drive.AmberNaviDrive;
import pl.edu.agh.amber.navi.drive.NaviDriveHelper;
import pl.edu.agh.amber.navi.dto.NaviPoint;
import pl.edu.agh.amber.navi.eye.HokuyoNaviEye;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;
import pl.edu.agh.amber.navi.tool.SerialPortHelper;
import pl.edu.agh.amber.navi.track.HoluxNaviTrack;
import pl.edu.agh.amber.navi.track.NaviTrackHelper;
import pl.edu.agh.amber.navi.track.StarGazerNaviTrack;
import pl.edu.agh.amber.roboclaw.RoboclawProxy;
import pl.edu.agh.amber.stargazer.StarGazerProxy;

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

        driveHelper = new AmberNaviDrive(roboclawProxy);

        switch (NaviConfig.getTrackHelperType()) {
            case HOLUX:
                SerialPort holuxSerialPort = SerialPortHelper.getHoluxSerialPort(holuxPortName);
                trackHelper = new HoluxNaviTrack(holuxSerialPort);
                break;
            case STAR_GAZER:
                SerialPort starGazerSerialPort = SerialPortHelper.getStarGazerSerialPort(starGazerPortName);
                trackHelper = new StarGazerNaviTrack(starGazerSerialPort);
                break;
            default:
                throw new Exception("No track helper initialized");
        }

        SerialPort hokuyoSerialPort = SerialPortHelper.getHokuyoSerialPort(hokuyoPortName);
        eyeHelper = new HokuyoNaviEye(hokuyoSerialPort);
        Thread eyeThread = new Thread(eyeHelper);
        eyeThread.start();

        NaviAgent naviAgent = new NaviAgent(driveHelper, trackHelper, eyeHelper);
        naviAgent.addLastPoints(route);
        Thread agentThread = new Thread(naviAgent);
        agentThread.start();
    }
}
