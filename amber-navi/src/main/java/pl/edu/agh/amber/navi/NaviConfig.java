package pl.edu.agh.amber.navi;

import pl.edu.agh.amber.navi.tool.PropertiesHelper;

import java.util.Properties;

public class NaviConfig {


    public static enum HelperType {
        AMBER, HOLUX, HOKUYO, DUMMY
    }

    private static final Properties properties = PropertiesHelper.loadProperties(null, NaviMain.class, NaviConst.MAIN_PROPERTIES);

    public static String getHostname() {
        return PropertiesHelper.getProperty(properties, NaviConst.HOSTNAME, NaviConst.HOSTNAME_DEFAULT);
    }

    public static int getPort() {
        return Integer.parseInt(PropertiesHelper.getProperty(properties, NaviConst.PORT, NaviConst.PORT_DEFAULT));
    }

    public static String getTrack() {
        return PropertiesHelper.getProperty(properties, NaviConst.TRACK, NaviConst.TRACK_DEFAULT);
    }

    public static double getRefCenterHorizontal() {
        return Double.parseDouble(PropertiesHelper.getProperty(properties, NaviConst.REF_CENTER_HORIZONTAL, "0.0"));
    }

    public static double getRefCenterVertical() {
        return Double.parseDouble(PropertiesHelper.getProperty(properties, NaviConst.REF_CENTER_VERTICAL, "0.0"));
    }

    @SuppressWarnings("unused")
    public static HelperType getDriveHelperType() {
        String driverHelperType = PropertiesHelper.getProperty(properties, NaviConst.DRIVE_HELPER_TYPE, null);
        if (NaviConst.AMBER_TYPE.equals(driverHelperType)) {
            return HelperType.AMBER;
        }
        return HelperType.DUMMY;
    }

    @SuppressWarnings("unused")
    public static HelperType getTrackHelperType() {
        String trackHelperType = PropertiesHelper.getProperty(properties, NaviConst.TRACK_HELPER_TYPE, null);
        if (NaviConst.AMBER_TYPE.equals(trackHelperType)) {
            return HelperType.AMBER;
        } else if (NaviConst.HOLUX_TYPE.equals(trackHelperType)) {
            return HelperType.HOLUX;
        }
        return HelperType.DUMMY;
    }

    @SuppressWarnings("unused")
    public static HelperType getEyeHelperType() {
        String eyeHelperType = PropertiesHelper.getProperty(properties, NaviConst.EYE_HELPER_TYPE, null);
        if (NaviConst.HOKUYO_TYPE.equals(eyeHelperType)) {
            return HelperType.HOKUYO;
        }
        return HelperType.DUMMY;
    }

    public static String getHoluxPortName() {
        return PropertiesHelper.getProperty(properties, NaviConst.HOLUX_PORT_NAME, NaviConst.HOLUX_PORT_NAME_DEFAULT);
    }

    public static String getHokuyoPortName() {
        return PropertiesHelper.getProperty(properties, NaviConst.HOKUYO_PORT_NAME, NaviConst.HOKUYO_PORT_NAME_DEFAULT);
    }
}
