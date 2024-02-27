package gorbuno.mkbs.lab1.utils;

import java.io.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;

import org.apache.commons.io.FileUtils;

public class FsUtils {
    public static boolean areFileNamesEqual(File f1, File f2) {
        return f1.getName().equals(f2.getName()); // сравниваем равенство, двух имен файлов
    }
    public static String readFile(File file) throws IOException { // полностью считываем файл в строку
        Scanner scanner = new Scanner(Paths.get(file.getAbsolutePath())); // создаем сканер файла
        StringJoiner content = new StringJoiner("\n"); // создаем контейнер для строк файла
        while(scanner.hasNextLine()) {
            content.add(scanner.nextLine()); // считываем все строки
        }
        scanner.close(); // убиваем сканер
        return content.toString(); // соединяем строки, вставляя между ними переносы строки
    }
    public static boolean areFileNamesAndContentEqual(File f1, File f2) throws IOException {
        return f1.getName().equals(f2.getName()) && FileUtils.contentEquals(f1, f2); // проверяем что имена и содержимое файлов совпадают
    }
    public static void updateFile(File file, String fileName, String content) throws IOException { // перезаписываем файл, старую версию удаляем
            // Создаем новый файл с переданным именем
            File newFile = new File(fileName);

            // Перезаписываем содержимое файла
            FileWriter writer = new FileWriter(newFile);
            writer.write(content);
            writer.close();

            // Если удалось записать содержимое в новый файл, удаляем старый файл (если он существует)
            if (Objects.nonNull(file) && file.exists() && !file.getName().equals(newFile.getName())) {
                file.delete();
            }
    }
    public static String computeFileSha256HashCode(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            fis.close();
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
