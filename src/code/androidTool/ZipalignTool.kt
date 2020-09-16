package code.androidTool

import code.util.MyUtil

class ZipalignTool {

    /**
     * 4字节对齐
     * @signApkPath 签名的apk
     * @返回对齐后的apk
     */
    fun align(signApkPath: String, outApkPath: String) {
        var sdkDir = "/Users/fuheng/Library/Android/sdk"
        var execStr = "$sdkDir/build-tools/28.0.3/zipalign " +
                "-f " +
                "4 " + //4个字节对齐
                "$signApkPath " +
                "$outApkPath "
        MyUtil.exec(execStr)
    }
}