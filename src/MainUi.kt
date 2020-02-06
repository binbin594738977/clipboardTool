import base.BaseComponent
import component.ClipboardUi
import core.Config
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.awt.Insets
import javax.swing.Box
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingConstants

class MainUi : BaseComponent() {
    init {
        val verateBox = Box.createVerticalBox()
        verateBox.preferredSize = Dimension(width, height)
        add(verateBox)

        verateBox.add(clipboardLayout())
    }

    /**
     * 剪贴板
     */
    private fun clipboardLayout(): Component? {
        //按钮<android手机剪贴板操作>
        val btnClipboard = JPanel()
        btnClipboard.preferredSize = Dimension(width, 200)
        var btn = btnClipboard.add(JButton("android手机剪贴板操作")) as JButton
        btn.horizontalAlignment = SwingConstants.LEADING
        btn.margin = Insets(5, 5, 5, 5)
        btn.font = Font("字体", Font.BOLD, 25)
        btn.addActionListener {
            Main.getThreadQueue().post {
                ClipboardUi().setVisible(true)
                close()
            }
        }
        return btnClipboard
    }

    /**
     * 窗口的各种配置
     */
    override fun setting() {
        title = "工具箱:" + Config.VERSION //设置标题
        setLocation(200, 30) //设置开始出来的位置
        setSize(400, 800) //设置窗口大小
        isResizable = true //设置用户是否可以改变框架大小
        //jFrame.setIconImage();
        //设置关闭方式 如果不设置的话 关闭窗口之后不会退出程序
        defaultCloseOperation = EXIT_ON_CLOSE
    }
}