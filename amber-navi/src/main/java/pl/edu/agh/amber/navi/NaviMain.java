package pl.edu.agh.amber.navi;

import pl.edu.agh.amber.common.AmberClient;
import pl.edu.agh.amber.navi.agent.NaviAgent;
import pl.edu.agh.amber.navi.agent.NaviPoint;
import pl.edu.agh.amber.navi.drive.AmberNaviDrive;
import pl.edu.agh.amber.navi.drive.NaviDriveHelper;
import pl.edu.agh.amber.navi.eye.HokuyoNaviEye;
import pl.edu.agh.amber.navi.eye.NaviEyeHelper;
import pl.edu.agh.amber.navi.track.AmberNaviTrack;
import pl.edu.agh.amber.navi.track.NaviTrackHelper;
import pl.edu.agh.amber.ninedof.NinedofProxy;
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

    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            System.err.println("Usage: <hostname> <port> <track> [<refCenterHorizontal> <refCenterVertical>]");
        }

        NaviPoint referenceCenter = null;
        if (args.length > 5) {
            referenceCenter = new NaviPoint(parseInt(args[3]), parseInt(args[4]), 0.0);
        }

        String hostname = args[0];
        int port = parseInt(args[1]);
        List<NaviPoint> route = parseTrack(args[2], referenceCenter);

        AmberClient amberClient = new AmberClient(hostname, port);
        NinedofProxy ninedofProxy = new NinedofProxy(amberClient, 0);
        RoboclawProxy roboclawProxy = new RoboclawProxy(amberClient, 0);

        NaviDriveHelper driveHelper = new AmberNaviDrive(ninedofProxy, roboclawProxy);
        NaviTrackHelper trackHelper = new AmberNaviTrack(starGazerProxy, ninedofProxy);
        NaviEyeHelper eyeHelper = new HokuyoNaviEye();

        NaviAgent naviAgent = new NaviAgent(driveHelper, trackHelper, eyeHelper);
        naviAgent.addLastPoints(route);
        Thread thread = new Thread(naviAgent);
        thread.start();
    }
}
