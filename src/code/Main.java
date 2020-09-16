package code;

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
        threadQueue = ThreadQueue.createAndStart();
        BaseComponent jFrame = new ApkToolUi();
        jFrame.setVisible(true);//设置开始显
    }

    public static ThreadQueue getThreadQueue() {
        return threadQueue;
    }


}