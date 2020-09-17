package code.adb;

import java.util.List;

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
        return new ActivityManagerExec().startActivity(intent).exec();
    }

    public List<String> startService(Intent intent) {
        return new ActivityManagerExec().startService(intent).exec();
    }

    public List<String> sendBroadcast(Intent intent) {
        return new ActivityManagerExec().sendBroadcast(intent).exec();
    }

    public List<String> push(String filePath, String outPath) {
        return new PushExec().push(filePath, outPath).exec();
    }

    public List<String> install(String filePath) {
        return new InstallExec().install(filePath).exec();
    }


    interface AdbInterfeice {
        List<String> exec();
    }

    class InstallExec extends ExecStringBuilder implements AdbInterfeice {
        String mFilePath;

        public InstallExec() {
            super("install");
        }

        public InstallExec install(String filePath) {
            mFilePath = filePath;
            append("-r").append(filePath);
            return this;
        }

        /**
         * 执行
         */
        public List<String> exec() {
            return adbExec(toString());
        }
    }

    class PushExec extends ExecStringBuilder implements AdbInterfeice {
        String mFilePath;
        String mOutPath;

        public PushExec() {
            super("push");
        }

        public PushExec push(String filePath, String outPath) {
            mFilePath = filePath;
            mOutPath = outPath;
            append(filePath).append(outPath);
            return this;
        }

        /**
         * 执行
         */
        public List<String> exec() {
            return adbExec(toString());
        }
    }

    class ActivityManagerExec extends ExecStringBuilder implements AdbInterfeice {
        Intent mIntent;

        public ActivityManagerExec() {
            super("shell am");
        }

        public ActivityManagerExec startActivity(Intent intent) {
            mIntent = intent;
            append("start").append(intent);
            return this;
        }

        public ActivityManagerExec startService(Intent intent) {
            mIntent = intent;
            append("startservice").append(intent);
            return this;
        }

        public ActivityManagerExec sendBroadcast(Intent intent) {
            mIntent = intent;
            append("broadcast").append(intent);
            return this;
        }

        /**
         * 执行
         */
        public List<String> exec() {
            return adbExec(toString());
        }
    }
}
