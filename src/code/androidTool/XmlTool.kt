package code.androidTool

import code.util.MyUtil

class XmlTool {
    var AXMLPrinter = MyUtil.getResourcesFile("res/AXMLPrinter2.S.jar")

    fun parseXml(xmlPath: String, outXml: String) {
        val execStr = ("java -jar ${AXMLPrinter.absolutePath} " +
                "$xmlPath " +
                "$outXml "
                )
        MyUtil.exec(execStr)
    }

    fun genXml(xmlPath: String, outXml: String) {
        val execStr = ("java -jar ${AXMLPrinter.absolutePath} "
                + "$xmlPath "
//                + "-o $outXml "
                )
        MyUtil.exec(execStr)
    }
}