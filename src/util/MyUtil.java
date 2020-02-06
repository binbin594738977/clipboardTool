package util;

import com.sun.istack.internal.Nullable;
import core.Config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyUtil {
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static List<String> exec(String str) {
        List<String> arr = new ArrayList<>();
        InputStreamReader ir = null;
        LineNumberReader input = null;
        try {
            Process p = Runtime.getRuntime().exec(str);
            ir = new InputStreamReader(p.getInputStream());
            input = new LineNumberReader(ir);      //创建IO管道，准备输出命令执行后的显示内容
            String line;
            while ((line = input.readLine()) != null) {     //按行打印输出内容
//                System.out.println(line);
                arr.add(new String(line.getBytes(), "UTF-8"));
            }
        } catch (IOException e1) {
            e1.printStackTrace();
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
     * 得到运行的目录
     */
    public static File getRunDirectory() {
        return new File(new File("").getAbsolutePath());
    }

    /**
     * 得到输出目录
     */
    public static File getOutDir() {
        return new File(getRunDirectory().getAbsolutePath() + "/" + "out");
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

    public static void test() {
        System.out.println("java版本号：" + System.getProperty("java.version")); // java版本号
        System.out.println("Java提供商名称：" + System.getProperty("java.vendor")); // Java提供商名称
        System.out.println("Java提供商网站：" + System.getProperty("java.vendor.url")); // Java提供商网站
        System.out.println("jre目录：" + System.getProperty("java.home")); // Java，哦，应该是jre目录
        System.out.println("Java虚拟机规范版本号：" + System.getProperty("java.vm.specification.version")); // Java虚拟机规范版本号
        System.out.println("Java虚拟机规范提供商：" + System.getProperty("java.vm.specification.vendor")); // Java虚拟机规范提供商
        System.out.println("Java虚拟机规范名称：" + System.getProperty("java.vm.specification.name")); // Java虚拟机规范名称
        System.out.println("Java虚拟机版本号：" + System.getProperty("java.vm.version")); // Java虚拟机版本号
        System.out.println("Java虚拟机提供商：" + System.getProperty("java.vm.vendor")); // Java虚拟机提供商
        System.out.println("Java虚拟机名称：" + System.getProperty("java.vm.name")); // Java虚拟机名称
        System.out.println("Java规范版本号：" + System.getProperty("java.specification.version")); // Java规范版本号
        System.out.println("Java规范提供商：" + System.getProperty("java.specification.vendor")); // Java规范提供商
        System.out.println("Java规范名称：" + System.getProperty("java.specification.name")); // Java规范名称
        System.out.println("Java类版本号：" + System.getProperty("java.class.version")); // Java类版本号
        System.out.println("Java类路径：" + System.getProperty("java.class.path")); // Java类路径
        System.out.println("Java lib路径：" + System.getProperty("java.library.path")); // Java lib路径
        System.out.println("Java输入输出临时路径：" + System.getProperty("java.io.tmpdir")); // Java输入输出临时路径
        System.out.println("Java编译器：" + System.getProperty("java.compiler")); // Java编译器
        System.out.println("Java执行路径：" + System.getProperty("java.ext.dirs")); // Java执行路径
        System.out.println("操作系统名称：" + System.getProperty("os.name")); // 操作系统名称
        System.out.println("操作系统的架构：" + System.getProperty("os.arch")); // 操作系统的架构
        System.out.println("操作系统版本号：" + System.getProperty("os.version")); // 操作系统版本号
        System.out.println("文件分隔符：" + System.getProperty("file.separator")); // 文件分隔符
        System.out.println("路径分隔符：" + System.getProperty("path.separator")); // 路径分隔符
        System.out.println("直线分隔符：" + System.getProperty("line.separator")); // 直线分隔符
        System.out.println("操作系统用户名：" + System.getProperty("user.name")); // 用户名
        System.out.println("操作系统用户的主目录：" + System.getProperty("user.home")); // 用户的主目录
        System.out.println("当前程序所在目录：" + System.getProperty("user.dir")); // 当前程序所在目录
    }
}
