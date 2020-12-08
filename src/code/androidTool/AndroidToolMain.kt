package code.androidTool

import code.util.MLog
import code.util.ZipUtil
import java.io.File

object AndroidToolMain {
    @JvmStatic
    fun main(args: Array<String>) {
//        smali2dex()
//        packZip()
        apk2smali()
    }

    fun apk2smali() {
        MLog.log("开始反编译")
        val apkToolTool = ApkToolTool()
        val apkPath = "/Users/fuheng/code/IdeaProjects/AppToolGUI/services.jar"
        val outDir = "/Users/fuheng/code/IdeaProjects/AppToolGUI/apkout/smali"
        apkToolTool.decompile(apkPath, outDir)
    }

    fun smali2apk() {
        MLog.log("开始打包")
        val apkToolTool = ApkToolTool()
        val sourceDir = "/Users/fuheng/code/IdeaProjects/AppToolGUI/smali"
        val outApkPath = "/Users/fuheng/code/IdeaProjects/AppToolGUI/apkout/unsigned.apk"
        apkToolTool.bale(sourceDir, outApkPath)
    }

    fun signed() {
        MLog.log("开始签名")
        val unsignedApkPath = "/Users/fuheng/IdeaProjects/AppToolGUI/unZip/unZip.zip"
        val genSignedApkPath = "/Users/fuheng/IdeaProjects/AppToolGUI/apkout/signed.apk"
        val keystoreTool = KeystoreTool()
        keystoreTool.signatureApk(unsignedApkPath, genSignedApkPath)
    }

    fun smali2dex() {
        MLog.log("smali打包dex")
        val smaliDirPath = "/Users/fuheng/code/IdeaProjects/AppToolGUI/smali"
        val outDexPath = "/Users/fuheng/code/IdeaProjects/AppToolGUI/apkout/classes.dex"
        val smaliTool = SmaliTool()
        smaliTool.smaliToDex(smaliDirPath, outDexPath)
    }

    fun dex2smali() {
        MLog.log("smali打包dex")
        val smaliDirPath = "/Users/fuheng/IdeaProjects/AppToolGUI/smali4/"
        val dexPath = "/Users/fuheng/IdeaProjects/AppToolGUI/dex444.dex"
        val smaliTool = SmaliTool()
        smaliTool.dex2smali(dexPath, smaliDirPath)
    }

    fun packZip() {
        MLog.log("正在打zip包")
        val file = File("/Users/fuheng/IdeaProjects/AppToolGUI/unpack")
        ZipUtil.compress("/Users/fuheng/IdeaProjects/AppToolGUI/apk.zip", file.listFiles()!!)
    }


    fun parseXml() {
        val xmlPath = "/Users/fuheng/IdeaProjects/AppToolGUI/apkout/apk_out/AndroidManifest.xml"
        val outPath = "/Users/fuheng/IdeaProjects/AppToolGUI/apkout/AndroidManifest.xml"
        XmlTool().parseXml(xmlPath, outPath);
    }

    fun genXml() {
        val xmlPath = "/Users/fuheng/IdeaProjects/AppToolGUI/unZip/AndroidManifest.xml"
        val outPath = "/Users/fuheng/IdeaProjects/AppToolGUI/apkout/AndroidManifest.xml"
        XmlTool().genXml(xmlPath, outPath);
    }
}