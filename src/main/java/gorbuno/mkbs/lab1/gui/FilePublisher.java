package gorbuno.mkbs.lab1.gui;


import static gorbuno.mkbs.lab1.utils.GuiUtils.setupWindow;

import java.io.*;
import java.nio.file.StandardWatchEventKinds;
import java.util.*;
import javax.swing.*;

import gorbuno.mkbs.lab1.utils.FsListening;

public class FilePublisher extends JFrame implements FsListening {
    public JPanel filesPanel = new JPanel();
    public JPanel fileListPanel = new JPanel();
    public JButton newFileButton = new JButton("Cоздать");


    public JPanel filePanel = new JPanel();
    public JLabel fileNameLabel = new JLabel("Имя файла");
    public JTextField filenameField = new JTextField();
    public JLabel contentLabel = new JLabel("Содержимое");
    public JTextArea contentArea = new JTextArea();
    public JButton saveFileButton = new JButton("Cохранить");

    public List<JComponent> borderedItems = Arrays.asList(fileListPanel, filePanel, contentArea, filenameField);
    public FilePublisher(final String title) {
        super();
        setupWindow(this);
        this.add(filesPanel);
        this.add(filePanel);
        filesPanel.add(fileListPanel);
        filesPanel.add(newFileButton);
    }

    @Override
    public void processFsEvent(final StandardWatchEventKinds kind, final File changedFile) {

    }
}
