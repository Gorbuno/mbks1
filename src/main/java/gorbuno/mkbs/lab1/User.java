package gorbuno.mkbs.lab1;

import java.io.*;

import gorbuno.mkbs.lab1.gui.FilePublisher;

public class User {
    public static void main(String[] args) {
        File privateFolder = new File(args[0]); // получаем путь к папкам из параметров запуска
        File publicFolder = new File(args[1]);

        if (!privateFolder.exists() || !publicFolder.exists()) { // если одна их папок не существует то не запустится
            System.out.println("Enter correct folders on startup!");
            System.exit(0);
        }

        new FilePublisher("Публикатор", privateFolder, publicFolder); // если все ок то запускаем окошко
    }
}
