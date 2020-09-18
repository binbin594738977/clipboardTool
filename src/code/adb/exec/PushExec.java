package code.adb.exec;

import code.adb.ExecStringBuilder;

public class PushExec extends ExecStringBuilder {
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
}
