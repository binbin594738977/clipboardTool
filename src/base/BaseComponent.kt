package base

import core.Config
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import javax.swing.*

public abstract class BaseComponent() : JFrame() {

    init {
        addMenu()//添加menu//设置
        setting()
    }

    /**
     * 分割线
     */
    open fun line(): JPanel? {
        val p1 = JPanel()
        p1.preferredSize = Dimension(width - 100, 1)
        p1.background = Color.BLACK
        return p1
    }

    /**
     * 添加输入框
     */
    open fun addMyInputUI(
        component: JComponent,
        name: String,
        columns: Int,
        size: Int
    ): JTextField? { //--微信号组件--
        val p3 = JPanel()
        // 标签
        val nameUI = JLabel(name)
        nameUI.font = Font("字体", Font.BOLD, size)
        p3.add(nameUI)
        // 文本域
        val text = JTextField(columns)
        text.font = Font("字体", Font.BOLD, size)
        p3.add(text)
        component.add(p3)
        return text
    }


    /**
     * 窗口的各种配置
     */
    abstract fun setting()

    /**
     * 添加菜单栏
     */
    open fun addMenu() {
        val menubar = JMenuBar() //构造菜单栏
        //第一个菜单
        val menu1 = JMenu("关于")
        val item1_1 = JMenuItem("作者：fuheng")
        menu1.add(item1_1)
        menu1.addSeparator()
        val item1_2 = JMenuItem("版本：" + Config.VERSION)
        menu1.add(item1_2)
        menubar.add(menu1)
        jMenuBar = menubar //设置菜单栏
    }

    fun close() {
        dispose()
    }


}