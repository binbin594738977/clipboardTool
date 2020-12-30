package code.adb;

import java.io.File;
import java.util.List;

import code.core.Config;

public class AdbHelp {
    public static final String APP_INSTALL_MIMETYPE = "application/vnd.android.package-archive";
    public static final String APP_STREAM_MIMETYPE = "application/octet-stream";
    public static final String SDCARD_PHONE = "/sdcard/";

    public static AdbManager adbManager = new AdbManager();

    public static void setDeviceConnectionListener(AdbManager.DeviceConnectionListener mDeviceConnectionListener) {
        adbManager.setDeviceConnectionListener(mDeviceConnectionListener);
    }

    public static boolean checkDeviceConnection(){
       return adbManager.checkDeviceConnection();
    }

    public static List<String> startActivity(Intent intent) {
        return adbManager.startActivity(intent);
    }

    public static List<String> startService(Intent intent) {
        return adbManager.startService(intent);
    }

    public static List<String> sendBroadcast(Intent intent) {
        return adbManager.sendBroadcast(intent);
    }

    public static List<String> pushSdcard(File filePath) {
        return adbManager.push(filePath.getAbsolutePath(), SDCARD_PHONE + filePath.getName());
    }

    public static List<String> install(String filePath) {
        return adbManager.install(filePath);
    }

    public static List<String> startApkToolService() {
        return startService(new Intent(Config.APP_TOOL_ANDROID_PACKAGE, ".AppToolService"));
    }

    public static List<String> execADB(String exec) {
        if (exec.startsWith("adb")) {
            exec = exec.substring(3);
        }
        return adbManager.adbExec(exec);
    }


    /**
     * 打电话
     */
    public static List<String> callPhone(String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL");
        intent.setData("tel:" + phoneNumber);
        return startActivity(intent);
    }

    /**
     * 打开webview
     */
    public static List<String> startWebView(String url) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(url);
        return startActivity(intent);
    }

    /**
     * 安装
     *
     * @param androidApkPath 安装apk存放的路径
     */
    public static List<String> installApk(String androidApkPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(AndroidUri.fromPath(androidApkPath), APP_INSTALL_MIMETYPE);
        return startActivity(intent);
    }


}
