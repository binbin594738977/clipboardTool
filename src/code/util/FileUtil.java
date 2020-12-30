package code.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @title 文件替换
 * @date 2019年6月21日 上午9:46:03
 */
public class FileUtil {

    public static boolean replaceFileNameString(String filepath, String sourceStr, String targetStr) {
        //文件路径为空
        if (StringUtil.isEmpty(filepath)) return false;
        File file = new File(filepath);
        //文件不存在
        if (!file.exists()) return false;
        String name = file.getName();
        File replaceFile = new File(file.getParent(), name.replace(sourceStr, targetStr));
//        Log.d("replaceName: " + replaceFile.getAbsolutePath());
        return renameTo(file, replaceFile);
    }

    /***
     * 替换指定文件中的指定内容
     *
     * @param filepath
     *            文件路径
     * @param sourceStr
     *            文件需要替换的内容
     * @param targetStr
     *            替换后的内容
     * @return 替换成功返回true，否则返回false
     */
    public static boolean replaceFileContentString(String filepath, String sourceStr, String targetStr) {
        try {
            FileReader fis = new FileReader(filepath); // 创建文件输入流
            //BufferedReader br = new BufferedReader(fis);
            char[] data = new char[1024]; // 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder(); // 创建字符串构建器
            // fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O
            // 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
            while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);// 把数组转换成字符串
                //System.out.println(str);
                sb.append(str);
            }
            fis.close();// 关闭输入流
            // 从构建器中生成字符串，并替换搜索文本
            String str = sb.toString().replace(sourceStr, targetStr);
            FileWriter fout = new FileWriter(filepath);// 创建文件输出流
            fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
            fout.close();// 关闭输出流

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 读取文件的字符串
     */
    public static String readFileContentString(File file) {
        try {
            if (file == null || !file.exists()) return "";

            FileReader fis = new FileReader(file.getAbsolutePath()); // 创建文件输入流
            //BufferedReader br = new BufferedReader(fis);
            char[] data = new char[10240]; // 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder(); // 创建字符串构建器
            // fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O
            // 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
            while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);// 把数组转换成字符串
                //System.out.println(str);
                sb.append(str);
            }
            fis.close();// 关闭输入流
            // 从构建器中生成字符串，并替换搜索文本
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读取文件的字符串
     */
    public static String readFileContentString1(File file) {
        if (file == null || !file.exists()) return "";
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = new FileInputStream(file);//文件名
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }


    /**
     * 按照特定的编码格式转换文件内容成string
     *
     * @param file File
     * @return 目标String
     */
    public static String fileToString(File file) {
        try {
            return streamToString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final int FILE_STREAM_BUFFER_SIZE = 16 * 1024;

    /**
     * 按照特定的编码格式转换Stream成string
     *
     * @param is Stream源
     * @return 目标String
     */
    public static String streamToString(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] data = new byte[FILE_STREAM_BUFFER_SIZE];
        try {
            int count;
            while ((count = is.read(data)) > 0) {
                os.write(data, 0, count);
            }
            return new String(os.toByteArray(), UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            close(os);
            close(is);
        }
        return "";
    }

    /**
     * 关闭，并捕获IOException
     *
     * @param closeable Closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 文件或文件夹拷贝
     * 如果是文件夹拷贝 目标文件必须也是文件夹
     *
     * @param srcFile 源文件
     * @param dstFile 目标文件
     * @return
     */
    public static boolean copy(File srcFile, File dstFile) {
        if (!srcFile.exists()) { //源文件不存在
            return false;
        }

        if (srcFile.isDirectory()) { //整个文件夹拷贝
            if (dstFile.isFile()) {    //如果目标是文件，返回false
                return false;
            }

            if (!dstFile.exists()) {
                mkdirs(dstFile);
            }

            for (File f : srcFile.listFiles()) {
                if (!copy(f, new File(dstFile, f.getName()))) {
                    return false;
                }
            }
            return true;

        } else { //单个文价拷贝
            return copyFile(srcFile, dstFile);
        }

    }

    /**
     * 判断某个文件所在的文件夹是否存在，不存在时直接创建
     *
     * @param path
     */
    public static void parentFolder(String path) {
        File file = new File(path);
        String parent = file.getParent();

        File parentFile = new File(parent + File.separator);
        if (!parentFile.exists()) {
            mkdirs(parentFile);
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件，如果是目录，则生成该目录下的同名文件再拷贝
     */
    private static boolean copyFile(File srcFile, File destFile) {
        if (!destFile.exists()) {
            if (!mkdirs(destFile.getParentFile()) || !createNewFile(destFile)) {
                return false;
            }
        } else if (destFile.isDirectory()) {
            destFile = new File(destFile, srcFile.getName());
        }

        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            FileChannel src = in.getChannel();
            FileChannel dst = out.getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utility.close(out);
            Utility.close(in);
        }

        return false;
    }

    /**
     * 创建目录（如果不存在）。
     *
     * @param dirPath 目录的路径
     * @return true表示创建，false表示该目录已经存在
     */
    public static boolean createDirIfMissed(String dirPath) {
        File dir = new File(dirPath);
        return !dir.exists() && dir.mkdirs();
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件，并且删除该目录
     *
     * @param path 将要删除的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中删除某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean deleteDir(String path) {
        return deleteDir(new File(path));
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件，并且删除该目录
     *
     * @param dir 将要删除的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中删除某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     *
     * @param path 将要清空的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(String path) {
        return clearDir(path, null);
    }

    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     *
     * @param path    将要清空的文件目录
     * @param excepts 除去这些目录或者文件，可以为null
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(String path, List<String> excepts) {
        ArrayList<File> exceptFiles = new ArrayList<>();
        if (excepts != null) {
            for (String except : excepts) {
                exceptFiles.add(new File(except));
            }
        }
        return clearDir(new File(path), exceptFiles);
    }

    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     *
     * @param dir 将要清空的文件目录
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(File dir) {
        return clearDir(dir, null);
    }

    /**
     * 递归清空目录下的所有文件及子目录下所有文件，但不删除目录（包括子目录）
     *
     * @param dir     将要清空的文件目录
     * @param excepts 除去这些目录或者文件，可以为null
     * @return boolean 成功清除目录及子文件返回true；
     * 若途中清空某一文件或清除目录失败，则终止清除工作并返回false.
     */
    public static boolean clearDir(File dir, List<File> excepts) {
        if (dir == null) {
            return false;
        }

        if (excepts != null && excepts.contains(dir)) {
            return true;
        }

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = clearDir(new File(dir, child), excepts);
                if (!success) {
                    return false;
                }
            }
            return true;
        }

        return dir.delete();
    }


    /**
     * 获取某个目录下所有文件的大小之和
     *
     * @param path
     * @return
     */
    public static float getDirSize(String path, boolean isRoot) {
        return getDirSize(path, null, isRoot);
    }

    /**
     * 获取某个目录下所有文件的大小之和
     *
     * @param path
     * @param excepts 除去这些目录或者文件，可以为null
     * @return
     */
    public static float getDirSize(String path, List<String> excepts, boolean isRoot) {
        if (StringUtil.isEmpty(path)) {
            return 0.f;
        }
        ArrayList<File> exceptFiles = new ArrayList<>();
        if (excepts != null) {
            for (String except : excepts) {
                exceptFiles.add(new File(except));
            }
        }
        return getDirSize(new File(path), exceptFiles, isRoot);
    }

    /**
     * 获取某个目录下所有文件的大小之和
     *
     * @return
     */
    public static float getDirSize(File dir, boolean isRoot) {
        return getDirSize(dir, null, isRoot);
    }

    /**
     * 获取某个目录下所有文件的大小之和
     *
     * @param excepts 除去这些目录或者文件，可以为null
     * @return
     */
    public static float getDirSize(File dir, List<File> excepts, boolean isRoot) {
        float size = 0.f;

        if (dir == null) {
            return size;
        }

        if (excepts != null && excepts.contains(dir)) {
            return size;
        }

        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] fs = dir.listFiles();
                for (File childFile : fs) {
                    if (childFile.isFile()) {
                        size += childFile.length();
                    } else {
                        size += getDirSize(childFile, excepts, false);
                    }
                }
            } else {
                if (!isRoot) {
                    size += dir.length();
                }
            }
        }

        return size;
    }


    /**
     * 删除文件。如果删除失败，则打出error级别的log
     *
     * @param file 文件
     * @return 成功与否
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            return false;
        }
        boolean result = file.delete();
        if (!result) {
            MLog.log("FileUtil cannot delete file: " + file);
        }
        return result;
    }

    /**
     * 创建文件。如果创建失败，则打出error级别的log
     *
     * @param file 文件
     * @return 成功与否
     */
    public static boolean createNewFile(File file) {
        if (file == null) {
            return false;
        }

        boolean result;
        try {
            result = file.createNewFile() || file.isFile();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }

        if (!result) {
            MLog.log("FileUtil cannot create file: " + file);
        }
        return result;
    }

    /**
     * 创建目录。如果创建失败，则打出error级别的log
     *
     * @param file 文件
     * @return 成功与否
     */
    public static boolean mkdir(File file) {
        if (file == null) {
            return false;
        }
        if (!file.mkdir() && !file.isDirectory()) {
            MLog.log("FileUtil cannot make dir: " + file);
            return false;
        }
        return true;
    }

    /**
     * 创建文件对应的所有父目录。如果创建失败，则打出error级别的log
     *
     * @param file 文件
     * @return 成功与否
     */
    public static boolean mkdirs(File file) {
        if (file == null) {
            return false;
        }
        if (!file.mkdirs() && !file.isDirectory()) {
            MLog.log("FileUtil cannot make dirs: " + file);
            return false;
        }
        return true;
    }

    /**
     * 文件或目录重命名。如果失败，则打出error级别的log
     *
     * @param srcFile 原始文件或目录
     * @param dstFile 重命名后的文件或目录
     * @return 成功与否
     */
    public static boolean renameTo(File srcFile, File dstFile) {
        if (srcFile == null || dstFile == null) {
            return false;
        }
        if (!srcFile.renameTo(dstFile)) {
            MLog.log("FileUtil cannot rename " + srcFile + " to " + dstFile);
            return false;
        }

        return true;
    }


    public static String getSizeString(long size) {
        if (size <= 0) {
            return "0B";
        }
        final String[] units = {"B", "K", "M", "G"};
        final DecimalFormat format = new DecimalFormat("0.##");
        final int kilo = 1024;
        double s = size;
        String finalUnit = null;
        for (String unit : units) {
            if (s < kilo) {
                finalUnit = unit;
                break;
            }
            s /= kilo;
        }
        if (finalUnit == null) {
            finalUnit = "T";
        }
        return format.format(s) + finalUnit;
    }

    public static void writeToFile(String content, String filename) throws Exception {
        FileWriter fw = new FileWriter(filename, false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.newLine();
        bw.write(content);
        bw.close();
        fw.close();
    }

    /**
     * 写字符串到文件 (文件不存在会创建)
     */
    public static void writeStringToFile(String content, File file, boolean append) {
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


    public static String getFromFile(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        reader.close();
        return stringBuilder.toString();
    }

    /**
     * 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

}