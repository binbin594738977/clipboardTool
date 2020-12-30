package code.util;

import java.io.File;
import java.io.InputStream;

public class ResourcesUtil {
    /**
     * 得到资源的流文件(在jar包时没法直接得到文件路径,只能先转成流,然后导出)
     *
     * @param sourcesPath
     * @return
     */
    public static InputStream getResourcesInputStream(String sourcesPath) {
        return ResourcesUtil.class.getClassLoader().getResourceAsStream(sourcesPath);
    }


    public static void toFile(String sourcesPath, File toFile) {
        InputStream resourcesInputStream = getResourcesInputStream(sourcesPath);
        //目录不存在就创建
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        //将资源导入到目录
        Utility.streamToFile(resourcesInputStream, toFile);
        MLog.log("导出完毕,现在文件是否存在: " + toFile.exists());
    }
}
