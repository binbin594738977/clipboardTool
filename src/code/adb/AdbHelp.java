package code.adb;

import java.io.File;
import java.util.List;

public class AdbHelp {
    public static final String APP_INSTALL_MIMETYPE = "application/vnd.android.package-archive";
    public static final String APP_STREAM_MIMETYPE = "application/octet-stream";
    public static final String SDCARD_PHONE = "/sdcard/";
    public static AdbManager adbManager = new AdbManager();


    /**
     * 设备连接的监听器
     */
    public static void setDeviceConnectionListener(AdbManager.DeviceConnectionListener mDeviceConnectionListener) {
        adbManager.setDeviceConnectionListener(mDeviceConnectionListener);
    }

    /**
     * 检查设备是否连接
     */
    public static boolean checkDeviceConnection() {
        return adbManager.checkDeviceConnection();
    }


    /**
     * 打开activity
     */
    public static List<String> startActivity(Intent intent) {
        return adbManager.startActivity(intent);
    }


    /**
     * 开启服务
     */
    public static List<String> startService(Intent intent) {
        return adbManager.startService(intent);
    }


    /**
     * 发送广播
     */
    public static List<String> sendBroadcast(Intent intent) {
        return adbManager.sendBroadcast(intent);
    }


    /**
     * adb push xxx /sdcard/xxx
     */
    public static List<String> pushSdcard(File filePath) {
        return adbManager.push(filePath.getAbsolutePath(), SDCARD_PHONE + filePath.getName());
    }


    /**
     * 安装adb install xxx
     */
    public static List<String> install(String filePath) {
        return adbManager.install(filePath);
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

    /**
     * 执行adb命令
     */
    public static List<String> execADB(String exec) {
        if (exec.startsWith("adb")) {
            exec = exec.substring(3);
        }
        return adbManager.adbExec(exec);
    }

}
