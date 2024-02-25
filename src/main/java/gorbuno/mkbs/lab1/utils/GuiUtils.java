package gorbuno.mkbs.lab1.utils;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import gorbuno.mkbs.lab1.gui.FilePublisher;

public class GuiUtils {
    public static void setupWindow(FilePublisher window) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setSize(660, 350);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        window.filesPanel.setBounds(20, 25, 300, 300);
        window.fileListPanel.setBounds(0, 0, 300, 250);
        window.newFileButton.setBounds(0, 255, 70, 40);
        window.filePanel.setBounds(340, 25, 300, 300);

        for (JComponent item : window.borderedItems) {
            item.setBorder(new LineBorder(Color.BLACK, 2));
        }

        window.setVisible(true);
    }
}
