package code.core;

public class Config {
    public static final String VERSION = "1.4";
    public static final String NAME = "AppToolOut";

    private static final String HOST = "";
    private static final String TEST_HOST = "";

    private static final String DEVICE_HOST = "";
    private static final String DEVICE_TEST_HOST = "";

    private static final String PROVIDE_HOST = "";
    private static final String PROVIDE_TEST_HOST = "";

    public static boolean DEBUG = false;

    /**
     * 得到机器人协议的host
     */
    public static String getRobotHost() {
        if (DEBUG) {
            return TEST_HOST;
        } else {
            return HOST;
        }
    }

    /**
     * 得到主控设备协议的host
     */
    public static String getDeviceHost() {
        if (DEBUG) {
            return DEVICE_TEST_HOST;
        } else {
            return DEVICE_HOST;
        }
    }

    /**
     * 得到接口协议的host
     */
    public static String getProvideHost() {
        if (DEBUG) {
            return PROVIDE_TEST_HOST;
        } else {
            return PROVIDE_HOST;
        }
    }
}
