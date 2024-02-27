package gorbuno.mkbs.lab1.gui;

import static gorbuno.mkbs.lab1.utils.FsUtils.areFileNamesAndContentEqual;
import static gorbuno.mkbs.lab1.utils.FsUtils.areFileNamesEqual;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import javax.swing.*;

public class FileListCellRenderer extends DefaultListCellRenderer {
    ImageIcon publicIcon = new ImageIcon("src/main/resources/icons/public.png");
    ImageIcon oldIcon = new ImageIcon("src/main/resources/icons/old.png");
    ImageIcon privateIcon = new ImageIcon("src/main/resources/icons/private.png");
    File folder;

    public FileListCellRenderer(final File folder) {
        super();
        this.folder = folder; // устанавливаем для рисовальщика папку с которой он будет сравнивать файлы
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String addition;
        File[] publicFiles = folder.listFiles();

        if (!(value instanceof File) || Objects.isNull(publicFiles)) { // заглушка от ошибок когда папка пуста
            return label;
        }

        File file = (File) value;
        if (Stream.of(publicFiles).anyMatch(f -> areFileNamesEqual(f, file))) { // если в публичной папке есть файл с таким же именем
            if (Stream.of(publicFiles).anyMatch(f -> { // если есть файл с таким же именем и содержимым
                try {
                    return areFileNamesAndContentEqual(f, file);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            })) {
                label.setIcon(publicIcon);                  // устанавливаем иконку
                addition = " (опубликован)";                // добавляем пояснение
            } else {
                label.setIcon(oldIcon);                     // устанавливаем иконку
                addition = " (изменен после публикации)";   // добавляем пояснение
            }
        } else {
            label.setIcon(privateIcon);                 // устанавливаем иконку
            addition = " (приватный)";                  // добавляем пояснение
        }
        label.setText(file.getName() + addition);

        return label;
    }
}
