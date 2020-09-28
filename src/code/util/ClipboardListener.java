package code.util;


import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardListener extends Thread implements ClipboardOwner {

    Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public void run() {
        Transferable selection = systemClipboard.getContents(this);
        gainOwnership(selection);
    }

    public void gainOwnership(Transferable t) {
        try {
            this.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        systemClipboard.setContents(t, this);
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        try {
            System.out.println("-----");
            System.out.println((String) clipboard.getData(DataFlavor.stringFlavor));
        } catch (UnsupportedFlavorException e) {
        } catch (IOException e) {
        }
        gainOwnership(contents);
    }

    /**
     * 从剪贴板中获取文本（粘贴）
     */
    public static String getClipboardString() {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 把文本设置到剪贴板（复制）
     */
    public static void setClipboardString(String text) {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(text);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }
}