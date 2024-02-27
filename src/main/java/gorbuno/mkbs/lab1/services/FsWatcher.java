package gorbuno.mkbs.lab1.services;

import static gorbuno.mkbs.lab1.utils.FsUtils.computeFileSha256HashCode;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;

public class FsWatcher implements Runnable {
    private File publicFolder;
    private File hackerFolder;

    public FsWatcher(File publicFolder, File hackerFolder) {
        this.publicFolder = publicFolder;
        this.hackerFolder = hackerFolder;
    }


    @Override
    public void run() {
        try {
            // получаем путь к публичной паке в виде объекта
            Path path = Paths.get(publicFolder.getAbsolutePath());
            // создаем наблюдатель за папкой
            WatchService watchService = FileSystems.getDefault().newWatchService();
            // настраиваем на отслеживание добавлений и изменений
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watchService.take();                 // ловим события изменения /добавления файла
                for (WatchEvent<?> event : key.pollEvents()) {      // для каждого события
                    WatchEvent<Path> ev = (WatchEvent<Path>) event; // извлекаем событие
                    Path fileName = ev.context();                   // из события извлекаем имя файла
                    File changedFile = new File(path + File.separator + fileName); // собираем полное имя файла
                    if (changedFile.isFile()) {                     // если файл не папка
                        tryCommitFile(changedFile);                 // пробуем его скопировать
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            System.out.println("Наблюдение за папкой прервано");
        }
    }


    private void tryCommitFile(File file) {
        File targetFolder = new File(hackerFolder.getAbsolutePath() + File.separator + file.getName());
        // если целевая папка не существует и не может быть создана либо является файлом - отменяем копирование
        if (!targetFolder.exists() && !targetFolder.mkdir() || targetFolder.exists() && targetFolder.isFile()) {
            System.out.println("При попытке доступа к целевой папке возникла ошибка");
            return;
        }
        String hashName = computeFileSha256HashCode(file);  // собираем имя файла на основе его содержимого
        if (hashName.isBlank()) { // если получаем пустую строку значит возникла ошибка - отменяем копирование
            System.out.println("При попытке анализа файла возникла ошибка");
            return;
        }
        // если нет в папке файлов с таким именем то копируем файл
        if (Stream.of(targetFolder.listFiles()).noneMatch(f -> f.getName().equals(hashName))) {
            String newFilePath = targetFolder.getAbsolutePath() + File.separator + hashName + ".txt"; // собираем имя файла который будет создан
            try {
                FileUtils.copyFile(file, new File(newFilePath)); // копируем файл
                System.out.println("Шалость удалась - Скопирован файл: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("При копировании файла возникла ошибка");
            }
        }
    }
}
