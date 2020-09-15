package code.androidTool

import code.util.MLog

object AndroidToolMain {
    @JvmStatic
    fun main(args: Array<String>) {
        MLog.log("开始反编译")
//        val apkToolTool = ApkToolTool()
//        val apkPath = "/Users/fuheng/Downloads/kuaiya.apk"
//        val outDir = "/Users/fuheng/IdeaProjects/ApkToolJava/out/apk_out"
//        apkToolTool.decompile(apkPath, outDir)
//
//        MLog.log("开始打包")
//        val sourceDir = outDir
//        val outApkPath = "/Users/fuheng/IdeaProjects/ApkToolJava/out/unsigned.apk"
//        apkToolTool.bale(sourceDir, outApkPath)

        MLog.log("开始签名")
        var unsignedApkPath = "/Users/fuheng/IdeaProjects/ApkToolJava/out/kuaiya333.apk"
        var genSignedApkPath = "/Users/fuheng/IdeaProjects/ApkToolJava/out/signed.apk"
        val keystoreTool = KeystoreTool()
        keystoreTool.signatureApk(unsignedApkPath, genSignedApkPath)
    }
}