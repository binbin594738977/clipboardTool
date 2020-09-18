package code.adb.exec;

import code.adb.ExecStringBuilder;

public class InstallExec extends ExecStringBuilder {
    String mFilePath;

    public InstallExec() {
        super("install");
    }

    public InstallExec install(String filePath) {
        mFilePath = filePath;
        append("-r").append(filePath);
        return this;
    }
}
