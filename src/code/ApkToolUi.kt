package code


import com.google.gson.Gson
import com.google.gson.JsonObject
import code.dialog.MyDialog
import code.util.MLog
import code.util.MyUtil
import code.util.StringUtil
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.Font
import java.awt.Insets
import java.awt.event.ActionListener
import java.io.File
import javax.swing.*

class ApkToolUi : BaseComponent {
    constructor() {
        layout = FlowLayout()
        val jpanel = JPanel()
        jpanel.layout = BoxLayout(jpanel, BoxLayout.Y_AXIS)
        jpanel.add(sehClipboardUI1())
        add(jpanel)
    }

    var jdeviceIdButton = JButton("设备ID: (点击选择)")
    val contentView = JTextArea()

    //设备id
    var mDeviceId = ""

    override fun setting() {
        title = "android手机剪贴板操作"
        setLocation(200, 30) //设置开始出来的位置
        setSize(800, 800) //设置窗口大小
        isResizable = true //设置用户是否可以改变框架大小
        //jFrame.setIconImage();
        //设置关闭方式 如果不设置的话 关闭窗口之后不会退出程序
        defaultCloseOperation = EXIT_ON_CLOSE
    }


    fun sehClipboardUI1(): JPanel? {
        val width = this.width - 100
        val height = 700
        val p1 = JPanel()
        p1.preferredSize = Dimension(width, height)
        val verateBox = Box.createVerticalBox()
        addVerticalBoxChildContainer(verateBox)
        verateBox.preferredSize = Dimension(width, height)
        p1.add(verateBox)
        return p1
    }

    /**
     * 添加竖向盒子子容器
     */
    private fun addVerticalBoxChildContainer(verateBox: Box) {
        addTitleView(verateBox)//添加标题
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        addSelectDeviceView(verateBox)//添加横向容器
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        addHorizontalBox(verateBox)//添加横向容器
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        addContentView(verateBox)//添加内容显示区
    }

    /**
     * 标题
     */
    private fun addTitleView(verateBox: Box) {
        //文字<使用方式>
        val descTextPanel = JPanel()
        val textField = JTextField()
        textField.setEditable(false)
        textField.preferredSize = Dimension(width, 80)
        textField.font = Font("字体", Font.BOLD, 20)
        textField.text = "本操作依赖adb操作,需要电脑配置sdk以及环境变量"
        textField.horizontalAlignment = JTextField.CENTER
        descTextPanel.add(textField)
        verateBox.add(descTextPanel)
    }

    private fun addSelectDeviceView(verateBox: Box) {
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
        verateBox.add(btnSelectDevice)
    }

    /**
     * 横向容器
     */
    private fun addHorizontalBox(verateBox: Box) {
        val p2 = JPanel()
        p2.preferredSize = Dimension(width, 250)
        val horizontalBox = Box.createHorizontalBox()
        //添加两个竖向容器
        addVerticalBox1(horizontalBox)
        addVerticalBox2(horizontalBox)
        p2.add(horizontalBox)
        verateBox.add(p2)
    }

    /**
     * 文字<显示内容的框框>
     */
    private fun addContentView(verateBox: Box) {
        contentView.lineWrap = true
        contentView.setMargin(Insets(5, 5, 5, 5))
        contentView.preferredSize = Dimension(width, 100)
        contentView.font = Font("字体", Font.BOLD, 20)
        verateBox.add(contentView)
    }

    /**
     * 竖向容器1
     */
    private fun addVerticalBox1(horizontalBox: Box) {
        val p3 = JPanel()
        p3.preferredSize = Dimension(width / 2, height)
        val verateBox = Box.createVerticalBox()
        addVerticalBox1Child(verateBox)
        p3.add(verateBox)
        horizontalBox.add(p3)
    }


    /**
     * 竖向容器2
     */
    private fun addVerticalBox2(horizontalBox: Box) {
        val p3 = JPanel()
        p3.preferredSize = Dimension(width / 2, height)
        val verateBox = Box.createVerticalBox()
        addVerticalBox2Child(verateBox)
        p3.add(verateBox)
        horizontalBox.add(p3)
    }

    private fun addVerticalBox1Child(verateBox: Box) {
        //按钮<安装apkTool>
        val btnInstallApk = getDefualtBtn("安装apkTool", ActionListener { contentView.text = installApkTool() })
        //按钮<设置剪贴板>
        val btnSetClipboard = getDefualtBtn("设置剪贴板内容", ActionListener { setClipboard(contentView.getText()) })
        //按钮<得到剪贴板>
        val btnGetClipboard = getDefualtBtn("得到剪贴板内容", ActionListener { contentView.text = getClipboard() })
        verateBox.add(btnInstallApk)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnSetClipboard)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnGetClipboard)
    }


    private fun addVerticalBox2Child(verateBox: Box) {
//        //按钮<安装指定apk>
        val btnInstallApk = getDefualtBtn("安装自定义apk", ActionListener { installApk() })
        //按钮<执行shell命令>
        val btnShell = getDefualtBtn("执行adb命令", ActionListener { exADB() })
        verateBox.add(btnInstallApk)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnShell)

    }

    fun getDefualtBtn(text: String, l: ActionListener): JPanel {
        val btnJPanel = JPanel()
        val btn = btnJPanel.add(JButton(text)) as JButton
        btn.margin = Insets(5, 5, 5, 5)
        btn.font = Font("字体", Font.BOLD, 25)
        btn.horizontalAlignment = SwingConstants.CENTER
        btn.addActionListener({
            Main.getThreadQueue().post {
                l.actionPerformed(it)
            }
        })
        return btnJPanel
    }
//---------------------------------------------------------------------




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
        MyUtil.exec("adb ${deviceExec} shell am startservice com.fh.apptool/.ApkToolService")
        MyUtil.exec("adb ${deviceExec} shell am broadcast -a clipper.set -e text '$text'")
        MyDialog.show("设置成功")
    }

    fun getClipboard(): String {
        try {
            val deviceExec = getDeviceExec()
            if (StringUtil.isEmpty(deviceExec)) {
                MyDialog.show("失败:设备没有连接")
                return ""
            }
            MyUtil.exec("adb ${deviceExec} shell am startservice com.fh.apptool/.ApkToolService")
            val execResult = MyUtil.exec("adb ${deviceExec} shell am broadcast -a clipper.get")
            if (execResult != null && execResult.size == 2) {
                var str = execResult.get(1)
                val top = "Broadcast completed: "
                str = str.substring(top.length)
                MLog.log("剪贴板原始内容==>" + str)

                val fromJson = Gson().fromJson("{${str}}", JsonObject::class.java)
                val data = fromJson.get("data").asString
                //按行打印输出内容
                MLog.log("剪贴板内容==>" + data)
                MyDialog.show("获取成功")
                return data
            }
        } catch (e: Exception) {
            MLog.log(e)
        }
        MyDialog.show("失败")
        return ""
    }

    fun installApkTool(): String {
        var result = ""
        try {
            val deviceExec = getDeviceExec()
            if (StringUtil.isEmpty(deviceExec)) {
                MyDialog.show("失败:没有连接设备")
                return ""
            }
            MyDialog.show("正在安装...")
            val apkFilePath = MyUtil.getResourcesFile("apk/apktool.apk").absolutePath
            val exec = MyUtil.exec("adb ${deviceExec} install -r ${apkFilePath}")
            result = "完成"
            for (s in exec) {
                result = result + "\n\r" + s
            }
            if (result.contains("Failure [INSTALL_FAILED_USER_RESTRICTED]")) {
                MyDialog.showLong("无法用usb直接安装,已经push到sdcard目录,请手动安装...")
                MyUtil.exec("adb ${deviceExec} push $apkFilePath /sdcard")
            } else if (result.contains("Success")) {
                MyDialog.show("安装完成")
            }
        } catch (e: Exception) {
            MLog.log(e)
        }
        return result
    }


    private fun installApk() {
        val deviceExec = getDeviceExec()
        if (StringUtil.isEmpty(deviceExec)) {
            MyDialog.show("失败:没有连接设备")
            return
        }
        val text = contentView.text.trim()
        if (StringUtil.isEmpty(text) || !File(text).exists()) {
            MyDialog.show("请填写apk路径")
            return
        }
        val file = File(text)
        if (!file.exists()) {
            MyDialog.show("文件路径错误")
            return
        }
        val apkPath = file.absolutePath
        MLog.log("apkPath: $apkPath")
        MyDialog.show("正在push")
        MyUtil.exec("adb ${deviceExec} shell am start -n com.fh.apptool/.MainActivity")
        MyUtil.exec("adb ${deviceExec} push $apkPath /sdcard")
        MyUtil.exec("adb ${deviceExec} shell am startservice com.fh.apptool/.ApkToolService")
        MyUtil.exec("adb ${deviceExec} shell am broadcast -a apktool.install -e text '/sdcard/${file.name}'")
        MyDialog.show("push完成,正在安装,请手动确认")
    }


    /**
     * 执行adb命令
     */
    private fun exADB() {
        val deviceExec = getDeviceExec()
        if (StringUtil.isEmpty(deviceExec)) {
            MyDialog.show("失败:没有连接设备")
            return
        }
        val text = contentView.text.trim()
        if (!StringUtil.isEmpty(text)) {
            if (text.startsWith("adb")) {
                val shell = "adb ${deviceExec} ${text.substring(3)}"
                MyUtil.exec(shell)
                MyDialog.show("成功")
            }
        } else {
            MyDialog.show("没有命令")
        }
    }

}
