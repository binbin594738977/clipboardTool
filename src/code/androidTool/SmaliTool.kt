package code.androidTool

import code.util.MyUtil
import java.io.File

class SmaliTool {
    var smaliJar = MyUtil.getResourcesFile("res/smali-2.3.4.jar")

    fun smaliToDex(smaliDirPath: String, outDexPath: String) {
        val execStr = ("java -jar ${smaliJar.absolutePath} " +
                "a $smaliDirPath " +
                "-o $outDexPath"
                )
        MyUtil.exec(execStr)
    }

}