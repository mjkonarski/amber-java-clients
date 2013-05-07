package pl.edu.agh.amber.navi;

public final class NaviConst {

    public static final String MAIN_PROPERTIES = "main.properties";

    public static final String HOSTNAME_DEFAULT = "127.0.0.1";

    public static final String PORT_DEFAULT = "1234";

    public static final String TRACK_DEFAULT = "/tmp/track.file";

    public static final String AMBER_TYPE = "amber";

    public static final String HOLUX_TYPE = "holux";

    public static final String HOKUYO_TYPE = "hokuyo";

    public static final String HOLUX_PORT_NAME_DEFAULT = "/dev/ttyACM0";

    public static final String HOKUYO_PORT_NAME_DEFAULT = "/dev/ttyACM1";


    private NaviConst() {
    }

    public static final String PREFIX = "pl.edu.agh.amber.navi.";

    public static final String HOSTNAME = PREFIX + "hostname";

    public static final String PORT = PREFIX + "port";

    public static final String TRACK = PREFIX + "track";

    public static final String REF_CENTER_HORIZONTAL = PREFIX + "refCenterHorizontal";

    public static final String REF_CENTER_VERTICAL = PREFIX + "refCenterVertical";

    public static final String DRIVE_HELPER_TYPE = PREFIX + "driveHelperType";

    public static final String TRACK_HELPER_TYPE = PREFIX + "trackHelperType";

    public static final String EYE_HELPER_TYPE = PREFIX + "eyeHelperType";

    public static final String HOLUX_PORT_NAME = PREFIX + "holuxPortName";

    public static final String HOKUYO_PORT_NAME = PREFIX + "hokuyoPortName";
}
