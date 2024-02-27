package gorbuno.mkbs.lab1.gui;

import static gorbuno.mkbs.lab1.utils.FsUtils.readFile;
import static gorbuno.mkbs.lab1.utils.FsUtils.updateFile;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.List;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

import org.apache.commons.io.FileUtils;

public class FilePublisher extends JFrame {
    private File privateFolder;
    private File publicFolder;
    public JPanel leftPanel = new JPanel(null);
    public JPanel fileListPanel = new JPanel(new BorderLayout());
    public DefaultListModel<File> listModel = new DefaultListModel<>();
    private JList<File> fileList = new JList<>(listModel);
    public JButton newFileButton = new JButton("Cоздать");
    public JButton refreshButton = new JButton("Обновить");
    public JPanel rightPanel = new JPanel(null);
    public JLabel fileNameLabel = new JLabel("Имя файла");
    public JTextField filenameField = new JTextField();
    public JLabel contentLabel = new JLabel("Содержимое");
    public JTextArea contentArea = new JTextArea();
    public JButton saveFileButton = new JButton("Cохранить");
    public JButton publishButton = new JButton("Опубликовать");
    public List<JComponent> borderedItems = Arrays.asList(fileListPanel, rightPanel, contentArea, filenameField);

    public FilePublisher(final String title, File privateFolder, File publicFolder) {
        super(title);
        this.privateFolder = privateFolder;
        this.publicFolder = publicFolder;
        setupUiComponents();
        composeUiComponents();
        setupActions();
        setupWindow();
        refresh();
    }

    public void setupUiComponents() {
        // определяем положение по x y для каждого элемента
        leftPanel.setBounds(20, 25, 300, 300);
        fileListPanel.setBounds(0, 0, 300, 250);
        newFileButton.setBounds(0, 255, 100, 20);
        refreshButton.setBounds(105, 255, 100, 20);
        rightPanel.setBounds(340, 25, 300, 250);
        fileNameLabel.setBounds(5, 7, 150, 25);
        filenameField.setBounds(0, 35, 300, 27);
        saveFileButton.setBounds(195, 75, 100, 20);
        publishButton.setBounds(45, 75, 145, 20);
        contentLabel.setBounds(5, 107, 200, 25);
        contentArea.setBounds(0, 135, 300, 115);

        // для некоторых элементов (указанных в списке borderedItems) добавляем черную обводку
        for (JComponent item : borderedItems) {
            item.setBorder(new LineBorder(Color.BLACK, 2));
        }

        // настраиваем отображение файлов в через добавление элемента ответственного за отрисовку
        fileList.setCellRenderer(new FileListCellRenderer(publicFolder));
    }

    public void composeUiComponents() {
        // вкладываем элементы друг в лруга
        this.add(leftPanel);
        this.add(rightPanel);
        leftPanel.add(fileListPanel);
        leftPanel.add(newFileButton);
        leftPanel.add(refreshButton);
        rightPanel.add(fileNameLabel);
        rightPanel.add(filenameField);
        rightPanel.add(saveFileButton);
        rightPanel.add(publishButton);
        rightPanel.add(contentLabel);
        rightPanel.add(contentArea);
        fileListPanel.add(new JScrollPane(fileList), BorderLayout.CENTER);
    }

    public void setupWindow() {
        // настраиваем режим позиционирования элементов в окне (null = по x y)
        setLayout(null);
        // настраиваем размеры окна
        setSize(670, 370);
        // запрещаем пользователю менять размер окна
        setResizable(false);
        // задаем положение окна (на усмотрение ОС)
        setLocationRelativeTo(null);
        // определяем действие при закрытии (завершить процесс)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent arg0) { }

            @Override
            public void windowIconified(WindowEvent arg0) { }

            @Override
            public void windowDeiconified(WindowEvent arg0) { }

            @Override
            public void windowActivated(WindowEvent arg0) { }

            @Override
            public void windowDeactivated(WindowEvent arg0) { }

            @Override
            public void windowClosing(WindowEvent arg0) { }

            @Override
            public void windowClosed(WindowEvent arg0) {
                System.exit(0);
            }
        });
        //отображаем окно программы
        this.setVisible(true);
    }

    public void setupActions() {
        // нажатие кнопки "Обновить" -> Обновление списка файлов
        refreshButton.addActionListener(e -> refresh());
        // выбор файла в списке -> отображение правой панели + активация кнопки "Опубликовать"
        fileList.addListSelectionListener(e -> {
            int selectedIndex = fileList.getSelectedIndex(); //смотрим какой файл выбран
            if (selectedIndex >= 0) { //если выбран любой то
                File selectedValue = listModel.getElementAt(selectedIndex); // достаем файл
                filenameField.setText(selectedValue.getName()); // отображаем его имя
                try {
                    contentArea.setText(readFile(selectedValue)); // отображаем его содержимое
                } catch (IOException ex) {
                    System.out.println("Ошибка чтения выбранного файла");
                    refresh();
                }
                rightPanel.show(); // отображаем правую панель
                publishButton.show(); // и кнопку "Опубликовать"
                saveFileButton.hide(); // "Сохранить" прячем ибо сохранять пока нечего
            }
        });
        // Нажатие "создать" -> отображение правой панели + очистка полей + сброс выбранного документа
        newFileButton.addActionListener(ev -> {
            fileList.clearSelection(); // сбрасываем выбор документа
            rightPanel.setVisible(true); // показываем правую панель
            filenameField.setText(""); // очищаем поля
            contentArea.setText("");
            publishButton.hide(); // прячем лишние кнопки
            saveFileButton.show();
        });
        // Нажатие "Сохранить" - перезапись выбранного файла / запись нового
        saveFileButton.addActionListener(ev -> {
            int selectedIndex = fileList.getSelectedIndex(); //смотрим какой файл выбран
            String targetFileName = privateFolder.getAbsolutePath() + File.separator + filenameField.getText(); //собираем имя сохраняемого файла
            // если никакой файл не выбран то ничего не перезаписываем, просто создаем новое
            File rewrittenFile = selectedIndex < 0 ? null : listModel.getElementAt(selectedIndex);
            try {
                updateFile(rewrittenFile, targetFileName, contentArea.getText()); // записываем новый файл + удаляем старый если нужно
            } catch (IOException e) {
                System.out.println("Ошибка Сохранения файла " + targetFileName);
            }
            refresh();
        });
        // нажатие "Опубликовать" -> копирует файл в публичную папку
        publishButton.addActionListener(ev -> {
            File source = listModel.getElementAt(fileList.getSelectedIndex()); // получаем выбранный файл из списка
            File target = new File(publicFolder.getAbsolutePath() + File.separator + source.getName()); // создаем файл назначения для копирования
            try {
                FileUtils.copyFile(source, target); // копируем
            } catch (IOException e) {
                System.out.println("Ошибка копирования при публикации файла");
            }
            refresh();
        });
        // настраиваем поле для содержимого файла, чтобы при любом вводе появилась кнопка "Сохранить"
        ((javax.swing.text.AbstractDocument) contentArea.getDocument()).setDocumentFilter(new InputCallbackFilter(false, this));
        // включаем фильр символов + настраиваем поле для имени файла, чтобы при любом вводе появилась кнопка "Сохранить"
        ((javax.swing.text.AbstractDocument) filenameField.getDocument()).setDocumentFilter(new InputCallbackFilter(true, this));
    }

    private void refresh() {
        // прячем правую панель
        rightPanel.setVisible(false);

        // обновляем список файлов
        listModel.clear();
        File[] files = privateFolder.listFiles();
        if (files != null) {
            listModel.addAll(List.of(files));
        }

        // обновляем/перерисовываем интерфейс
        this.revalidate();
        this.repaint();
    }
}
