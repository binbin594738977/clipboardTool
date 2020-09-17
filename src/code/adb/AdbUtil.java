package code.adb;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.List;

import code.util.MyUtil;

public class AdbUtil {
    public static final String APP_INSTALL_MIMETYPE = "application/vnd.android.package-archive";
    public static final String APP_STREAM_MIMETYPE = "application/octet-stream";
    public static final String APP_TOOL_DIR = "/sdcard/appTool";
    public static final String APP_TOOL_PACKAGE = "com.fh.apptool";
    public static AdbManager adbManager = new AdbManager();

    public static void main(String[] args) {
        installApp("/sdcard/hefeixin_4.0_release.apk");
    }

    public static void setDeviceId(String deviceId) {
        adbManager.setDeviceId(deviceId);
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

    public static List<String> push(String filePath) {
        return adbManager.push(filePath, APP_TOOL_DIR);
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
    public static List<String> installApp(String androidApkPath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(AndroidUri.fromPath(androidApkPath), APP_INSTALL_MIMETYPE);
        return startActivity(intent);
    }
}
