package code.androidTool

import code.util.MyUtil

/**
usage: apktool
-advance,--advanced   prints advance information.
-version,--version    prints the version then exits
usage: apktool if|install-framework [options] <framework.apk>
-p,--frame-path <dir>   Stores framework files into <dir>.
-t,--tag <tag>          Tag frameworks using <tag>.
usage: apktool d[ecode] [options] <file_apk>
-f,--force              Force delete destination directory.
-o,--output <dir>       The name of folder that gets written. Default is apk.out
-p,--frame-path <dir>   Uses framework files located in <dir>.
-r,--no-res             Do not decode resources.
-s,--no-src             Do not decode sources.
-t,--frame-tag <tag>    Uses framework files tagged by <tag>.
usage: apktool b[uild] [options] <app_path>
-f,--force-all          Skip changes detection and build all files.
-o,--output <dir>       The name of apk that gets written. Default is dist/name.apk
-p,--frame-path <dir>   Uses framework files located in <dir>.
 */
class ApkToolTool {
    val apktool = MyUtil.getResourcesFile("res/apktool_2.4.1.jar")

    /**
     * 反编译
     */
    fun decompile(ApkPath: String, outDir: String) {
        val execStr = ("java -jar ${apktool.absolutePath} d $ApkPath " +
                "-r " +//是否不需要资源文件
                "-o $outDir"//输出
                )
        MyUtil.exec(execStr)
    }

    /**
     * 打包
     */
    fun bale(sourceDir: String, outApkPath: String) {
        val execStr = ("java -jar ${apktool.absolutePath} b $sourceDir " +
                "-o $outApkPath" //输出
                )
        MyUtil.exec(execStr)
    }

}