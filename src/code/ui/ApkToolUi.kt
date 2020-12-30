package code.ui


import code.Main
import code.adb.AdbHelp
import code.adb.AdbManager
import code.adb.Intent
import code.core.Config
import code.dialog.MyDialog
import code.util.MLog
import code.util.MyUtil
import code.util.StringUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.awt.Dimension
import java.awt.Font
import java.awt.Insets
import java.awt.datatransfer.*
import java.awt.event.ActionListener
import java.io.File
import java.io.IOException
import javax.swing.*


class ApkToolUi : BaseComponent {
    constructor() {
        getContentPane().add(MainUi())
        AdbHelp.setDeviceConnectionListener(mDeviceConnectionListener)
    }

    var mDeviceConnectionListener = object : AdbManager.DeviceConnectionListener {
        override fun changgeDevice(device: String?) {
            jdeviceIdButton.text = "设备ID: " + device
            MLog.log("设备改变了 ${device}")
        }

        override fun noConnection() {
            MyDialog.show("失败:设备没有连接")
        }

        override fun selectDevice(devices: MutableList<String>): Int {
            val selectIndex = JOptionPane.showOptionDialog(this@ApkToolUi, "请选择设备...", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, devices.toTypedArray(), null)
            if (selectIndex >= 0 && selectIndex < devices.size) {
                jdeviceIdButton.text = "设备ID: " + devices[selectIndex]
            }
            MLog.log("设备选择了 ${selectIndex}")
            return selectIndex
        }

    }

    var jdeviceIdButton = JButton("设备ID: (点击选择)")
    val contentView = JTextArea()


    //设备id
    var mDeviceId = ""

    override fun setting() {
        title = Config.PROJECT_NAME
        setLocation(200, 30) //设置开始出来的位置
        setSize(800, 800) //设置窗口大小
        isResizable = true //设置用户是否可以改变框架大小
        //jFrame.setIconImage();
        //设置关闭方式 如果不设置的话 关闭窗口之后不会退出程序
        defaultCloseOperation = EXIT_ON_CLOSE
    }


    fun MainUi(): JPanel? {
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
                AdbHelp.checkDeviceConnection()
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
        contentView.dragEnabled = true
        contentView.transferHandler = object : TransferHandler() {
            override fun importData(c: JComponent, t: Transferable): Boolean {
                try {
                    //是文件
                    if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        val fileListPath = t.getTransferData(DataFlavor.javaFileListFlavor) as List<*>
                        //此处输出的文件/文件夹的名字以及路径
                        MLog.log("文件名: $fileListPath")
                        contentView.text = contentView.text + fileListPath[0].toString()
                        return true
                    } else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        val str = t.getTransferData(DataFlavor.stringFlavor) as String
                        MLog.log("字符串: $str")
                        contentView.text = contentView.text + str
                        return true
                    } else {
                        MLog.log("未知操作")
                    }
                } catch (e: UnsupportedFlavorException) {
                    MLog.log(e)
                    return true
                } catch (e: IOException) {
                    MLog.log(e)
                } catch (e: Exception) {
                    MLog.log(e)
                }
                return false
            }

            override fun canImport(c: JComponent, flavors: Array<DataFlavor>): Boolean {
                return true
            }

            override fun exportToClipboard(comp: JComponent, clipboard: Clipboard, action: Int) {
                super.exportToClipboard(comp, clipboard, action)
                MLog.log("复制内容: " + contentView.selectedText)
                // 封装文本内容
                val trans: Transferable = StringSelection(contentView.selectedText)
                // 把文本内容设置到系统剪贴板
                clipboard.setContents(trans, null)
            }
        }
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
        val btnInstallApk = getDefualtBtn("安装apkTool", ActionListener { installApkTool() })
        //按钮<设置剪贴板>
        val btnSetClipboard = getDefualtBtn("设置剪贴板内容", ActionListener { setClipboard() })
        //按钮<得到剪贴板>
        val btnGetClipboard = getDefualtBtn("得到剪贴板内容", ActionListener { getClipboard() })
        verateBox.add(btnInstallApk)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnSetClipboard)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnGetClipboard)
    }


    private fun addVerticalBox2Child(verateBox: Box) {
//        //按钮<系统安装器安装apk>
        val btnSystemInstallApk = getDefualtBtn("系统安装器安装apk", ActionListener { systemInstallApk() })
        //按钮<Apptool安装apk>
        val btnApptoolInstallApk = getDefualtBtn("Apptool安装apk", ActionListener { apptoolInstallApk() })
        //按钮<执行shell命令>
        val btnShell = getDefualtBtn("执行adb命令", ActionListener { exADB() })
        verateBox.add(btnSystemInstallApk)
        verateBox.add(Box.createVerticalStrut(15));    //添加高度为15的垂直框架
        verateBox.add(btnApptoolInstallApk)
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
                //检查设备连接状态
                if (!AdbHelp.checkDeviceConnection()) {
                    return@post
                }
                l.actionPerformed(it)
            }
        })
        return btnJPanel
    }
//---------------------------------------------------------------------

    fun setClipboard() {
        val text = contentView.getText()
        AdbHelp.startApkToolService()
        AdbHelp.sendBroadcast(Intent("clipper.set").putExtras("text", text))
        MyDialog.show("设置成功")
    }

    fun getClipboard() {
        try {
            AdbHelp.startApkToolService()
            val execResult = AdbHelp.sendBroadcast(Intent("clipper.get"))
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
                contentView.text = data
                return
            }
        } catch (e: Exception) {
            MLog.log(e)
        }
        MyDialog.show("失败")
    }

    fun installApkTool() {
        var result = ""
        try {
            if (!AdbHelp.checkDeviceConnection()) {
                return
            }
            MyDialog.show("正在安装...")
            val apkFile = MyUtil.getResourcesFile(Config.APP_TOOL_SRC_PATH)
            if (!apkFile.exists()) {
                MyDialog.show("文件不存在...")
                return
            }
            MLog.log("apkFile: $apkFile")
            MyDialog.show("正在push")
            AdbHelp.pushSdcard(apkFile)
            AdbHelp.installApk("${AdbHelp.SDCARD_PHONE}${apkFile.name}")
            MyDialog.show("push完成,正在安装,请手动确认")
        } catch (e: Exception) {
            MLog.log(e)
        }
    }


    private fun systemInstallApk() {
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
        MLog.log("apkPath: $file")
        MyDialog.show("正在push")
        AdbHelp.pushSdcard(file)
        AdbHelp.installApk("${AdbHelp.SDCARD_PHONE}${file.name}")
        MyDialog.show("push完成,正在安装,请手动确认")
    }

    private fun apptoolInstallApk() {
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
        MLog.log("apkPath: $file")
        MyDialog.show("正在push")
        AdbHelp.pushSdcard(file)
        AdbHelp.startApkToolService()
        AdbHelp.sendBroadcast(Intent("apktool.install").putExtras("text", "${AdbHelp.SDCARD_PHONE}${file.name}"))
        MyDialog.show("push完成,正在安装,请手动确认")
    }

    /**
     * 执行adb命令
     */
    private fun exADB() {
        val text = contentView.text.trim()
        if (!StringUtil.isEmpty(text)) {
            AdbHelp.execADB(text.substring(3))
            MyDialog.show("成功")
        } else {
            MyDialog.show("没有命令")
        }
    }
}

