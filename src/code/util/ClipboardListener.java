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
}