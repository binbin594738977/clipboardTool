package code.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class CmdUtil {
    public static List<String> exec(String str) {
        MLog.log(" ---------------------------------开始命令-----------------------------------");
        MLog.log("| 命令: " + str);
        List<String> arr = new ArrayList<>();
        InputStreamReader ir = null;
        LineNumberReader input = null;
        BufferedReader bufrError = null;
        try {
//            String os_name = System.getProperty("os.name");
//            if (os_name != null && os_name.contains("Mac") && str.startsWith("adb")) {
//                str = "/Users/fuheng/Library/Android/sdk/platform-tools/" + str;
//            }
            Process p = Runtime.getRuntime().exec(str);
            ir = new InputStreamReader(p.getInputStream());
            bufrError = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
            input = new LineNumberReader(ir);      //创建IO管道，准备输出命令执行后的显示内容
            String line;
            while ((line = input.readLine()) != null) {     //按行打印输出内容
                MLog.log("| " + line);
                arr.add(new String(line.getBytes(), "UTF-8"));
            }
            while ((line = bufrError.readLine()) != null) {
                arr.add(new String(line.getBytes(), "UTF-8"));
            }
            MLog.log(" ---------------------------------执行结束-----------------------------------\r\n\r\n");
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

}
