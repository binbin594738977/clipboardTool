package component


import base.BaseComponent
import com.google.gson.Gson
import com.google.gson.JsonObject
import dialog.MyDialog
import util.MyUtil
import java.awt.*
import javax.swing.*

class ClipboardUi() : BaseComponent() {

    init {
        layout = FlowLayout()
        val jpanel = JPanel()
        jpanel.layout = BoxLayout(jpanel, BoxLayout.Y_AXIS)
        jpanel.add(sehClipboardUI1())
        add(jpanel)
    }

    fun sehClipboardUI1(): JPanel? {
        val width = this.width - 100
        val height = 600
        val p1 = JPanel()
        p1.preferredSize = Dimension(width, height)
        val verateBox = Box.createVerticalBox()
        verateBox.preferredSize = Dimension(width, height)
        p1.add(verateBox)
        //文字<使用方式>
        val descTextPanel = JPanel()
        val textField = JTextField()
        textField.setEditable(false)
        textField.preferredSize = Dimension(width, 100)
        textField.font = Font("字体", Font.BOLD, 20)
        textField.text = "本操作依赖adb操作,需要电脑配置jdk以及环境变量"
        textField.horizontalAlignment = JTextField.CENTER
        descTextPanel.add(textField)
        //文字<内容>
        val textArea = JTextArea()
        textArea.lineWrap = true
        textArea.setMargin(Insets(5, 5, 5, 5))
        textArea.preferredSize = Dimension(width, 100)
        textArea.font = Font("字体", Font.BOLD, 20)
        //按钮<安装apk>
        val btnInstallApk = JPanel()
        var btn = btnInstallApk.add(JButton("安装必要apk")) as JButton
        btn.margin = Insets(5, 5, 5, 5)
        btn.font = Font("字体", Font.BOLD, 25)
        btn.horizontalAlignment = SwingConstants.CENTER
        btn.addActionListener {
            Main.getThreadQueue().post {
                textArea.text = installApk()
                MyDialog.show("正在安装...")
            }
        }
        //按钮<设置剪贴板>
        val btnSetClipboard = JPanel()
        btn = btnSetClipboard.add(JButton("设置剪贴板内容")) as JButton
        btn.margin = Insets(5, 5, 5, 5)
        btn.font = Font("字体", Font.BOLD, 25)
        btn.horizontalAlignment = SwingConstants.CENTER
        btn.addActionListener {
            Main.getThreadQueue().post {
                setClipboard(textArea.getText())
                MyDialog.show("设置成功")
            }
        }
        //按钮<得到剪贴板>
        val btnGetClipboard = JPanel()
        btn = btnGetClipboard.add(JButton("得到剪贴板内容")) as JButton
//        button.preferredSize = Dimension(width, 200)
        btn.margin = Insets(5, 5, 5, 5)
        btn.font = Font("字体", Font.BOLD, 25)
        btn.horizontalAlignment = SwingConstants.CENTER
        btn.addActionListener {
            Main.getThreadQueue().post {
                textArea.text = getClipboard()
                MyDialog.show("得到剪贴版")
            }
        }
        verateBox.add(descTextPanel)
        verateBox.add(Box.createVerticalStrut(20));    //添加高度为20的垂直框架
        verateBox.add(btnInstallApk)
        verateBox.add(Box.createVerticalStrut(20));    //添加高度为20的垂直框架
        verateBox.add(btnSetClipboard)
        verateBox.add(Box.createVerticalStrut(20));    //添加高度为20的垂直框架
        verateBox.add(btnGetClipboard)
        verateBox.add(Box.createVerticalStrut(20));    //添加高度为20的垂直框架
        verateBox.add(textArea)
        return p1
    }

    fun setClipboard(text: String) {
        MyUtil.exec("adb shell am startservice ca.zgrs.clipper/.ClipboardService")
        MyUtil.exec("adb shell am broadcast -a clipper.set -e text \"$text\"")
    }

    fun getClipboard(): String {
        MyUtil.exec("adb shell am startservice ca.zgrs.clipper/.ClipboardService")
        val execResult = MyUtil.exec("adb shell am broadcast -a clipper.get")
        if (execResult != null && execResult.size == 2) {
            var str = execResult.get(1)
            val top = "Broadcast completed: "
            str = str.substring(top.length)
            println("剪贴板内容11==>" + str)
            val fromJson = Gson().fromJson<JsonObject>("{${str}}", JsonObject::class.java)
            val data = fromJson.get("data").asString
            //按行打印输出内容
            println("剪贴板内容==>" + data)
            return data;
        }
        return ""
    }

    fun installApk(): String {
        val apkFile = MyUtil.getResourcesFile("apk/clipper.apk")
        val exec = MyUtil.exec("adb install -r ${apkFile.absoluteFile}")
        var str = "完成"
        for (s in exec) {
            str = str + "\n\r" + s;
        }
        return str
    }

    override fun setting() {
        title = "android手机剪贴板操作"
        setLocation(200, 30) //设置开始出来的位置
        setSize(800, 700) //设置窗口大小
        isResizable = true //设置用户是否可以改变框架大小
        //jFrame.setIconImage();
        //设置关闭方式 如果不设置的话 关闭窗口之后不会退出程序
        defaultCloseOperation = EXIT_ON_CLOSE
    }
}