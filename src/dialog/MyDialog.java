package dialog;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import util.MyUtil;

@SuppressWarnings("serial")
public class MyDialog extends JWindow {

    private JLabel back = null;

    private static MyDialog dialog;

    public static void show(String str) {
        if (dialog == null) {
            dialog = new MyDialog(str);
        }
        dialog.back.setText(str);
        dialog.setVisible(false);
        dialog.setVisible(true);
        MyUtil.timerDelayed(new TimerTask() {
            public void run() {
                dialog.setVisible(false);
                dialog.dispose();
            }
        }, 1000);
    }

    private MyDialog(String str) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        Dimension frameSize = this.getSize();
        int x = (screenWidth - frameSize.width) / 2;
        int y = (screenHeight - frameSize.height) / 2;
        back = new JLabel(str);
        back.setForeground(Color.WHITE);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.setSize(getMaximumSize());
        setSize((250 + (str.length() * 4)), 70);
        setLocation(x - ((250 + (str.length() * 4)) / 2), (y - 130 / 2));
        toFront();
        back.setBounds(10, 10, (250 + (str.length() * 4)) - 10, 50);
        setBackground(Color.BLACK);
        contentPane.setBackground(Color.BLACK);
        contentPane.add(back);
    }

}