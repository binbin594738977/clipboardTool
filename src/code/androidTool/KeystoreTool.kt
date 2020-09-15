package code.androidTool

import code.util.MyUtil

/**
 * todo 如果太大的话,需要删掉META-INF目录后然后签名
 * 提示：jarsigner: 无法对 jar 进行签名: java.util.zip.ZipException: invalid entry compressed size (expected 41278 but got 42575
bytes) bytes)
 */
class KeystoreTool {
    private val storePassword = "F7BfQ43=XkJF2Dg7G/*x"
    private val keyAlias = "v6game"
    private val keyPassword = "F7BfQ43=XkJF2Dg7G/*x"

    fun signatureApk(unsignedApkPath: String, genSignedApkPath: String) {
        val keystore = MyUtil.getResourcesFile("res/keystore.jks")
        val execStr = ("jarsigner -keystore ${keystore.absoluteFile}"
                + " -storepass " + storePassword
                + " -keypass " + keyPassword
                + " -digestalg SHA1 "
                + " -sigalg MD5withRSA "
                + " -signedjar $genSignedApkPath $unsignedApkPath $keyAlias")
        MyUtil.exec(execStr)
    }
}