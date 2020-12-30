package code.util;

public class EnvironmentUtil {
    /**
     * 用户主页目录
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }


    /**
     * 如果是jar包得到jar包的父目录,如果不是jar包,得到当前运行的目录
     */
    public static String getJarPathOrRunPath() {
        String path = EnvironmentUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1, path.length());
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        return path.replace("target/classes/", "");
    }
}
