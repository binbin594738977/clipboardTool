package component


import Main
import base.BaseComponent
import com.google.gson.Gson
import com.google.gson.JsonObject
import core.Config
import dialog.MyDialog
import util.MLog
import util.MyUtil
import util.StringUtil
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Insets
import javax.swing.*

class ClipboardUi() : BaseComponent() {
    var jdeviceIdButton = JButton("设备ID: (点击选择)")

    //设备id
    var mDeviceId = ""

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
        textField.text = "本操作依赖adb操作,需要电脑配置sdk以及环境变量"
        textField.horizontalAlignment = JTextField.CENTER
        descTextPanel.add(textField)
        //文字<显示内容的框框>
        val textArea = JTextArea()
        textArea.lineWrap = true
        textArea.setMargin(Insets(5, 5, 5, 5))
        textArea.preferredSize = Dimension(width, 100)
        textArea.font = Font("字体", Font.BOLD, 20)
        //按钮<安装apk>
        val btnSelectDevice = JPanel()

        var btn = btnSelectDevice.add(jdeviceIdButton) as JButton
        btn.margin = Insets(5, 5, 5, 5)
        btn.font = Font("字体", Font.BOLD, 25)
        btn.horizontalAlignment = SwingConstants.CENTER
        btn.addActionListener {
            Main.getThreadQueue().post {
                selectDeviceId()
            }
        }
        //按钮<安装apk>
        val btnInstallApk = JPanel()
        btn = btnInstallApk.add(JButton("安装必要apk")) as JButton
        btn.margin = Insets(5, 5, 5, 5)
        btn.font = Font("字体", Font.BOLD, 25)
        btn.horizontalAlignment = SwingConstants.CENTER
        btn.addActionListener {
            Main.getThreadQueue().post {
                textArea.text = installApk()
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
            }
        }
        verateBox.add(descTextPanel)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnSelectDevice)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnInstallApk)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnSetClipboard)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnGetClipboard)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(textArea)
        return p1
    }

    /**
     * 选择设备id,只有一个设备不会弹框
     * @return 如果选择了设备,返回设备id , 没有返回""
     */
    private fun selectDeviceId(): String {
        val devices = getDevices()
        if (devices.size == 0) {
            return ""
        } else if (devices.size == 1) {
            setDeviceId(devices.get(0))
            return devices.get(0)
        } else {
            //多个设备
            val selectIndex = JOptionPane.showOptionDialog(this, "请选择设备...", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, devices.toTypedArray(), null)
            MLog.log("选择了 ${selectIndex}")
            if (selectIndex != -1) {
                setDeviceId(devices.get(selectIndex))
                return devices.get(selectIndex)
            }
        }
        return ""
    }

    /**
     * 设置设备
     */
    fun setDeviceId(deviceId: String) {
        jdeviceIdButton.text = "设备ID: " + deviceId
        mDeviceId = deviceId
    }

    /**
     * 得到所以连接的设备
     */
    fun getDevices(): List<String> {
        val arrayList = ArrayList<String>()
        val results = MyUtil.exec("adb devices")
        if (results.size <= 2) {
            MyDialog.show("没有连接设备")
        } else {
            for (result in results) {
                val split = result.split("\t")
                if (split.size > 1) {
                    arrayList.add(split[0])
                    MLog.log("设备id: ${split[0]}")
                }
            }
        }
        return arrayList
    }

    /**
     * 得到选择设备的命令 -s c6de1086 , 没有设备会为""
     */
    fun getDeviceExec(): String {
        val devices = getDevices()
        if (devices.size == 0) {
            //没有设备连接
            return ""
        } else if (devices.size >= 2) {
            //多个设备连接,先判断是否由指定的设备
            //如果指定的deviceid包含在内,直接用
            if (devices.contains(mDeviceId)) {
                return "-s ${mDeviceId}"
            }
            //如果不包含,先选择
            return selectDeviceId()
        } else {
            //只有一个设备
            setDeviceId(devices.get(0))
            return "-s ${devices.get(0)}"
        }
    }

    fun setClipboard(text: String) {
        val deviceExec = getDeviceExec()
        if (StringUtil.isEmpty(deviceExec)) {
            MyDialog.show("失败:没有连接设备")
            return
        }
        MyUtil.exec("adb ${deviceExec} shell am startservice ca.zgrs.clipper/.ClipboardService")
        MyUtil.exec("adb ${deviceExec} shell am broadcast -a clipper.set -e text '$text'")
        MyDialog.show("设置成功")
    }

    fun getClipboard(): String {
        val deviceExec = getDeviceExec()
        if (StringUtil.isEmpty(deviceExec)) {
            MyDialog.show("失败:设备没有连接")
            return ""
        }
        MyUtil.exec("adb ${deviceExec} shell am startservice ca.zgrs.clipper/.ClipboardService")
        val execResult = MyUtil.exec("adb ${deviceExec} shell am broadcast -a clipper.get")
        if (execResult != null && execResult.size == 2) {
            var str = execResult.get(1)
            val top = "Broadcast completed: "
            str = str.substring(top.length)
            MLog.log("剪贴板内容11==>" + str)
            val fromJson = Gson().fromJson("{${str}}", JsonObject::class.java)
            val data = fromJson.get("data").asString
            //按行打印输出内容
            MLog.log("剪贴板内容==>" + data)
            MyDialog.show("成功:得到剪贴版")
            return data;
        }
        MyDialog.show("失败:得到剪贴版")
        return ""
    }

    fun installApk(): String {
        val deviceExec = getDeviceExec()
        if (StringUtil.isEmpty(deviceExec)){
            MyDialog.show("失败:没有连接设备")
            return ""
        }
        MyDialog.show("正在安装...")
        val apkFile = MyUtil.getResourcesFile("apk/clipper.apk")
        val exec = MyUtil.exec("adb ${deviceExec} install -r ${apkFile.absoluteFile}")
        var str = "完成"
        for (s in exec) {
            str = str + "\n\r" + s
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