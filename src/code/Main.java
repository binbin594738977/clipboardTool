package code;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import code.util.MLog;
import code.util.ThreadQueue;

public class Main {

    private static ThreadQueue threadQueue;

    /**
     * https://blog.csdn.net/pruett/article/details/72833130   Layout的详解
     */
    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("arg = " + arg);
        }
        if (args.length > 0) {
            MLog.setLogMod(Integer.parseInt(args[0]));
        }

        System.out.println("Default Charset=" + Charset.defaultCharset());

        System.out.println("file.encoding=" + System.getProperty("file.encoding"));

        System.out.println("Default Charset=" + Charset.defaultCharset());

        System.out.println("Default Charset in Use=" + getDefaultCharSet());

        threadQueue = ThreadQueue.createAndStart();
        BaseComponent jFrame = new ApkToolUi();
        jFrame.setVisible(true);//设置开始显
    }

    private static String getDefaultCharSet() {

        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());

        String enc = writer.getEncoding();

        return enc;

    }

    public static ThreadQueue getThreadQueue() {
        return threadQueue;
    }


}
