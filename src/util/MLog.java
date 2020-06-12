package util;

import java.io.File;

public class MLog {
    private static int logMod = 0;
    private final static File cacheFile = new File(MyUtil.getOutDir().getAbsolutePath() + "/" + "log.txt");

    public static void logWithArgs(String format, Object... args) {
        log(String.format(format, args));
    }

    public static void log(Object msg) {
        switch (logMod) {
            case 0:
                System.out.println(msg);
                break;
            case 1:
                MyUtil.printFile(msg.toString() + "\n\r", cacheFile, true);
                break;
        }

    }

    public static void setLogMod(int logMod) {
        MLog.logMod = logMod;
    }
}
