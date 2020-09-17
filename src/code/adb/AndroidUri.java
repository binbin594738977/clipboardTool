package code.adb;

import java.io.File;

public class AndroidUri {
    public static String fromPath(String filePath) {
        return "file://" + filePath;
    }
}
