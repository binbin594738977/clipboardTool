package code.androidTool

import code.util.MyUtil

class SmaliTool {
    var smaliJar = MyUtil.getResourcesFile("res/smali-2.4.0.jar")
    var baksmali = MyUtil.getResourcesFile("res/baksmali-2.3.4.jar")

    fun smaliToDex(smaliDirPath: String, outDexPath: String) {
        val execStr = ("java -jar ${smaliJar.absolutePath} " +
                "a $smaliDirPath " +
                "-o $outDexPath"
                )
        MyUtil.exec(execStr)
    }

    fun dex2smali(dexPath: String, smaliDirPath: String) {
        val execStr = ("java -jar ${baksmali.absolutePath} " +
                "d $dexPath " +
                "-o $smaliDirPath"
                )
        MyUtil.exec(execStr)
    }

}