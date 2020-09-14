package code.util;

import java.io.File;

public class MyEnvironment {
    /**
     * 用户主页目录
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     * 得到运行的目录
     */
    public static File getRunDirectory() {
        return new File(new File("").getAbsolutePath());
    }
}
