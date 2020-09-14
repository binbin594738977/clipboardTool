package util;

import com.sun.istack.internal.Nullable;

import core.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyUtil {
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static List<String> exec(String str) {
        MLog.log("-----------------------------------开始命令");
        MLog.log("命令: " + str);
        List<String> arr = new ArrayList<>();
        InputStreamReader ir = null;
        LineNumberReader input = null;
        try {
//            String os_name = System.getProperty("os.name");
//            if (os_name != null && os_name.contains("Mac") && str.startsWith("adb")) {
//                str = "/Users/fuheng/Library/Android/sdk/platform-tools/" + str;
//            }
            Process p = Runtime.getRuntime().exec(str);
            ir = new InputStreamReader(p.getInputStream());
            input = new LineNumberReader(ir);      //创建IO管道，准备输出命令执行后的显示内容
            String line;
            MLog.log("-----------------------------------结果start");
            while ((line = input.readLine()) != null) {     //按行打印输出内容
                MLog.log(line);
                arr.add(new String(line.getBytes(), "UTF-8"));
            }
            MLog.log("-----------------------------------结果end");
        } catch (IOException e1) {
            MLog.log(e1);
        } finally {
            try {
                if (ir != null) ir.close();
            } catch (IOException e) {
            }
            try {
                if (input != null) input.close();
            } catch (IOException e) {
            }
        }
        return arr;
    }


    public static List<String> exec1(String str) {
        List<String> arr = new ArrayList<>();
        List<String> command = new ArrayList<>();
        command.add("/bin/bash");
        command.add("-c");
        command.add("adb");
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while (in.read(re) != -1) {
                MLog.log("==>" + new String(re));
                arr.add(new String(re, "UTF-8"));
            }
            in.close();
            if (process.isAlive()) {
                process.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }


    /**
     * 文件夹里面的文件,文件名的指定字符串的改为指定修改的字符串
     */
    public static void containsRenameTo(String filePath, String contains, String replacement) {
        //想命名的原文件的路径
        File file = new File(filePath);
        File[] files = file.listFiles();
        System.out.println("files.length = " + files.length);
        for (File file1 : files) {
            if (!file1.isDirectory()) {
                String name = file1.getName();
                System.out.println("name = " + name);
                if (name.contains(contains)) {
                    String absolutePath = file1.getAbsolutePath();
                    String replaceFile = absolutePath.replace(contains, replacement);
                    file1.renameTo(new File(replaceFile));
                }
            }
        }
    }


    /**
     * 得到资源的流文件(在jar包时没法直接得到文件路径,只能先转成流,然后导出)
     *
     * @param sourcesPath
     * @return
     */
    public static InputStream getResourcesInputStream(String sourcesPath) {
        return Config.class.getClassLoader().getResourceAsStream(sourcesPath);
    }

    /**
     * 得到输出目录
     */
    public static File getOutDir() {
        return new File(MyEnvironment.getUserHome() + "/" + Config.NAME);
    }

    /**
     * 得到资源文件,如果本地没有,将资源导入根目录
     *
     * @param sourcesPath
     * @return
     */
    public static File getResourcesFile(String sourcesPath) {
        //查看当前目录是否存在
        File file = new File(getOutDir().getAbsolutePath() + "/" + sourcesPath);
        if (!file.exists()) {
            System.out.println("注意:有文件输出在->" + file.getAbsolutePath());
            InputStream resourcesInputStream = getResourcesInputStream(sourcesPath);
            //目录不存在就创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //将资源导入到目录
            Utility.streamToFile(resourcesInputStream, file);
        }
        return file;
    }

    /**
     * 打印File
     */
    public static void printFile(String content, File file, boolean append) {
        FileWriter fw = null;
        if (file == null) return;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, append);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(content, 0, content.length());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void timerDelayed(TimerTask task, long delayed) {
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(task, delayed);// 这里毫秒
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
