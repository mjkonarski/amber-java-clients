package pl.edu.agh.amber.navi;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;
import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.navi.agent.NaviAgent;
import pl.edu.agh.amber.navi.agent.NaviPoint;
import pl.edu.agh.amber.navi.drive.AmberNaviDrive;
import pl.edu.agh.amber.navi.drive.DummyNaviDrive;
import pl.edu.agh.amber.navi.drive.NaviDriveHelper;
import pl.edu.agh.amber.navi.eye.DummyNaviEye;
import pl.edu.agh.amber.navi.eye.HokuyoNaviEye;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;
import pl.edu.agh.amber.navi.tool.SerialPortHelper;
import pl.edu.agh.amber.navi.tool.SerialPortWrapper;
import pl.edu.agh.amber.navi.track.AmberNaviTrack;
import pl.edu.agh.amber.navi.track.DummyNaviTrack;
import pl.edu.agh.amber.navi.track.HoluxNaviTrack;
import pl.edu.agh.amber.navi.track.NaviTrackHelper;
import pl.edu.agh.amber.ninedof.NinedofProxy;
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

    private static final String OWNER = "navi";

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

    public static void main(String[] args) throws IOException, NoSuchPortException, PortInUseException,
            UnsupportedCommOperationException {

        NaviPoint referenceCenter = new NaviPoint(NaviConfig.getRefCenterHorizontal(),
                NaviConfig.getRefCenterVertical(), 0.0);

        String holuxPortName = NaviConfig.getHoluxPortName();
        String hokuyoPortName = NaviConfig.getHokuyoPortName();

        String hostname = NaviConfig.getHostname();
        int port = NaviConfig.getPort();
        List<NaviPoint> route = parseTrack(NaviConfig.getTrack(), referenceCenter);

        AmberClient amberClient = new AmberClient(hostname, port);
        NinedofProxy ninedofProxy = new NinedofProxy(amberClient, 0);
        RoboclawProxy roboclawProxy = new RoboclawProxy(amberClient, 0);
        StarGazerProxy starGazerProxy = new StarGazerProxy(amberClient, 0);

        NaviDriveHelper driveHelper;
        NaviTrackHelper trackHelper;
        NaviEyeHelper eyeHelper;

        switch (NaviConfig.getDriveHelperType()) {
            case AMBER:
                driveHelper = new AmberNaviDrive(roboclawProxy);
                break;
            default:
                driveHelper = new DummyNaviDrive();
                break;
        }
        switch (NaviConfig.getTrackHelperType()) {
            case AMBER:
                trackHelper = new AmberNaviTrack(starGazerProxy, ninedofProxy);
                break;
            case HOLUX:
                SerialPortWrapper holuxSerialPortWrapper = SerialPortHelper.getSerialPort(holuxPortName, OWNER, 30);
                trackHelper = new HoluxNaviTrack(holuxSerialPortWrapper);
                break;
            default:
                trackHelper = new DummyNaviTrack();
                break;
        }
        switch (NaviConfig.getEyeHelperType()) {
            case HOKUYO:
                SerialPortWrapper hokuyoSerialPortWrapper = SerialPortHelper.getSerialPort(hokuyoPortName, OWNER, 31);
                eyeHelper = new HokuyoNaviEye(hokuyoSerialPortWrapper);
                break;
            default:
                eyeHelper = new DummyNaviEye();
        }

        NaviAgent naviAgent = new NaviAgent(driveHelper, trackHelper, eyeHelper);
        naviAgent.addLastPoints(route);
        Thread thread = new Thread(naviAgent);
        thread.start();
    }
}
