package code;

import code.ui.ApkToolUi;
import code.ui.BaseComponent;
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



    public static void test() {
        MLog.log("java版本号：" + System.getProperty("java.version")); // java版本号
        MLog.log("Java提供商名称：" + System.getProperty("java.vendor")); // Java提供商名称
        MLog.log("Java提供商网站：" + System.getProperty("java.vendor.url")); // Java提供商网站
        MLog.log("jre目录：" + System.getProperty("java.home")); // Java，哦，应该是jre目录
        MLog.log("Java虚拟机规范版本号：" + System.getProperty("java.vm.specification.version")); // Java虚拟机规范版本号
        MLog.log("Java虚拟机规范提供商：" + System.getProperty("java.vm.specification.vendor")); // Java虚拟机规范提供商
        MLog.log("Java虚拟机规范名称：" + System.getProperty("java.vm.specification.name")); // Java虚拟机规范名称
        MLog.log("Java虚拟机版本号：" + System.getProperty("java.vm.version")); // Java虚拟机版本号
        MLog.log("Java虚拟机提供商：" + System.getProperty("java.vm.vendor")); // Java虚拟机提供商
        MLog.log("Java虚拟机名称：" + System.getProperty("java.vm.name")); // Java虚拟机名称
        MLog.log("Java规范版本号：" + System.getProperty("java.specification.version")); // Java规范版本号
        MLog.log("Java规范提供商：" + System.getProperty("java.specification.vendor")); // Java规范提供商
        MLog.log("Java规范名称：" + System.getProperty("java.specification.name")); // Java规范名称
        MLog.log("Java类版本号：" + System.getProperty("java.class.version")); // Java类版本号
        MLog.log("Java类路径：" + System.getProperty("java.class.path")); // Java类路径
        MLog.log("Java lib路径：" + System.getProperty("java.library.path")); // Java lib路径
        MLog.log("Java输入输出临时路径：" + System.getProperty("java.io.tmpdir")); // Java输入输出临时路径
        MLog.log("Java编译器：" + System.getProperty("java.compiler")); // Java编译器
        MLog.log("Java执行路径：" + System.getProperty("java.ext.dirs")); // Java执行路径
        MLog.log("操作系统名称：" + System.getProperty("os.name")); // 操作系统名称
        MLog.log("操作系统的架构：" + System.getProperty("os.arch")); // 操作系统的架构
        MLog.log("操作系统版本号：" + System.getProperty("os.version")); // 操作系统版本号
        MLog.log("文件分隔符：" + System.getProperty("file.separator")); // 文件分隔符
        MLog.log("路径分隔符：" + System.getProperty("path.separator")); // 路径分隔符
        MLog.log("直线分隔符：" + System.getProperty("line.separator")); // 直线分隔符
        MLog.log("操作系统用户名：" + System.getProperty("user.name")); // 用户名
        MLog.log("操作系统用户的主目录：" + System.getProperty("user.home")); // 用户的主目录
        MLog.log("当前程序所在目录：" + System.getProperty("user.dir")); // 当前程序所在目录
    }

}
