package code.adb;

public class ExecStringBuilder {
    private final StringBuilder builder = new StringBuilder();

    public ExecStringBuilder(String exec) {
        append(exec);
    }

    public ExecStringBuilder append(String sign, String key, String value) {
        builder.append(sign);
        builder.append(" ");
        builder.append(key);
        builder.append(" '");
        builder.append(value);
        builder.append("' ");
        return this;
    }

    public ExecStringBuilder append(String sign, String key, int value) {
        builder.append(sign);
        builder.append(" ");
        builder.append(key);
        builder.append(" ");
        builder.append(value);
        builder.append(" ");
        return this;
    }

    public ExecStringBuilder append(String sign, String exec) {
        builder.append(sign);
        builder.append(" ");
        builder.append(exec);
        builder.append(" ");
        return this;
    }

    public ExecStringBuilder append(String exec) {
        builder.append(exec);
        builder.append(" ");
        return this;
    }

    public ExecStringBuilder append(Intent intent) {
        intent.builder(this);
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
