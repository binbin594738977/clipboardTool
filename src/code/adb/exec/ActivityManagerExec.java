package code.adb.exec;

import code.adb.ExecStringBuilder;
import code.adb.Intent;

public class ActivityManagerExec extends ExecStringBuilder {
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
}
