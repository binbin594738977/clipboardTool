package code.adb;

import java.util.ArrayList;
import java.util.List;

import code.adb.exec.ActivityManagerExec;
import code.adb.exec.InstallExec;
import code.adb.exec.PushExec;
import code.dialog.MyDialog;
import code.util.CmdUtil;
import code.util.MLog;
import code.util.StringUtil;

public class AdbManager {
    private static final String ADB = "adb ";
    private String mDeviceId = "";
    private DeviceConnectionListener mDeviceConnectionListener = new DeviceConnectionListener() {
        @Override
        public void noConnection() {

        }

        @Override
        public void changgeDevice(String device) {

        }

        @Override
        public int selectDevice(List<String> devices) {
            return 0;
        }
    };

    public List<String> adbExec(String exec) {
        StringBuilder sb = new StringBuilder();
        sb.append(ADB);
        if (!StringUtil.isEmpty(mDeviceId)) {
            sb.append("-s ");
            sb.append(mDeviceId);
            sb.append(" ");
        }
        sb.append(exec);
        return CmdUtil.exec(sb.toString());
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

    /**
     * 检查设备连接
     */
    public boolean checkDeviceConnection() {
        List<String> devices = getDevices();
        if (devices.size() == 0) {
            //没有设备连接
            if (mDeviceConnectionListener != null)
                mDeviceConnectionListener.noConnection();
            return false;

        } else if (devices.contains(mDeviceId)) {
            //如果包含当前device,直接用
            return true;
        } else if (devices.size() > 1) {
            //包含当前device,多个设备连接,选择设备
            int selectIndex = mDeviceConnectionListener.selectDevice(devices);
            MLog.log("选择了 " + selectIndex);
            if (selectIndex >= 0 && selectIndex < devices.size()) {
                setDeviceId(devices.get(selectIndex));
                return true;
            }
            //没有选择
            return false;
        } else {
            //只有一个设备,改变设备
            mDeviceConnectionListener.changgeDevice(devices.get(0));
            setDeviceId(devices.get(0));
            return true;
        }
    }

    /**
     * 得到所以连接的设备
     */
    public List<String> getDevices() {
        List<String> arrayList = new ArrayList();
        List<String> results = CmdUtil.exec("adb devices");
        if (results.size() <= 2) {
            MyDialog.show("没有连接设备");
        } else {
            for (String result : results) {
                String[] split = result.split("\t");
                if (split.length > 1) {
                    arrayList.add(split[0]);
                    MLog.log("设备id: " + split[0]);
                }
            }
        }
        return arrayList;
    }

    public void setDeviceConnectionListener(DeviceConnectionListener mDeviceConnectionListener) {
        if (mDeviceConnectionListener == null) return;
        this.mDeviceConnectionListener = mDeviceConnectionListener;
    }

    public DeviceConnectionListener getDeviceConnectionListener() {
        return mDeviceConnectionListener;
    }

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }


    public interface DeviceConnectionListener {
        void noConnection();

        void changgeDevice(String device);

        int selectDevice(List<String> devices);
    }
}
