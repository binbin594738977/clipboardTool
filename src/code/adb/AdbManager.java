package code.adb;

import java.util.List;

import code.adb.exec.ActivityManagerExec;
import code.adb.exec.InstallExec;
import code.adb.exec.PushExec;
import code.util.MyUtil;
import code.util.StringUtil;

public class AdbManager {
    private static final String ADB = "adb ";
    private String mDeviceId = "";

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public List<String> adbExec(String exec) {
        StringBuilder sb = new StringBuilder();
        sb.append(ADB);
        if (!StringUtil.isEmpty(mDeviceId)) {
            sb.append("-s ");
            sb.append(mDeviceId);
            sb.append(" ");
        }
        sb.append(exec);
        return MyUtil.exec(sb.toString());
    }

    public List<String> startActivity(Intent intent) {
        return adbExec(new ActivityManagerExec().startActivity(intent).getExecString());
    }

    public List<String> startService(Intent intent) {
        return adbExec(new ActivityManagerExec().startService(intent).getExecString());
    }

    public List<String> sendBroadcast(Intent intent) {
        return adbExec(new ActivityManagerExec().sendBroadcast(intent).getExecString());
    }

    public List<String> push(String filePath, String outPath) {
        return adbExec(new PushExec().push(filePath, outPath).getExecString());
    }

    public List<String> install(String filePath) {
        return adbExec(new InstallExec().install(filePath).getExecString());
    }
}
