package code;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;

/**
 * @author wpskl
 */
public class GetFilePathSingle {
    public JPanel getPanel() {
        JPanel jp = new JPanel();
        JTextArea fileTarget = new JTextArea(200, 300);
        fileTarget.setDragEnabled(true);
        fileTarget.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            public boolean importData(JComponent c, Transferable t) {
                try {
                    List<String> o = (List<String>) t.getTransferData(DataFlavor.javaFileListFlavor);
//此处输出文du件/文件夹的名字以及路径
                    System.out.println("sdf" + o.toString());
                    return true;
                } catch (UnsupportedFlavorException ufe) {
                    ufe.printStackTrace();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean canImport(JComponent c, DataFlavor[] flavors) {
                return true;
            }
        });
        jp.add(fileTarget);
        return jp;
    }

    public static void main(String[] args) {
        final JFrame jf = new JFrame(" by wpskl ");
        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jf.getContentPane().add(new GetFilePathSingle().getPanel());
        jf.setSize(300, 400);
        jf.setVisible(true);
    }
}