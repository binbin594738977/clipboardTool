package core;

public class Config {
    public static final String VERSION = "1.1";
    public static final String TITLE = "工具箱";

    private static final String HOST = "";
    private static final String TEST_HOST = "";

    private static final String DEVICE_HOST = "";
    private static final String DEVICE_TEST_HOST = "";

    private static final String PROVIDE_HOST = "";
    private static final String PROVIDE_TEST_HOST = "";

    public static boolean hasTest = false;

    /**
     * 得到机器人协议的host
     */
    public static String getRobotHost() {
        if (hasTest) {
            return TEST_HOST;
        } else {
            return HOST;
        }
    }

    /**
     * 得到主控设备协议的host
     */
    public static String getDeviceHost() {
        if (hasTest) {
            return DEVICE_TEST_HOST;
        } else {
            return DEVICE_HOST;
        }
    }

    /**
     * 得到接口协议的host
     */
    public static String getProvideHost() {
        if (hasTest) {
            return PROVIDE_TEST_HOST;
        } else {
            return PROVIDE_HOST;
        }
    }


}
