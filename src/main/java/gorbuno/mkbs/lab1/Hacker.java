package gorbuno.mkbs.lab1;

import java.io.*;

import gorbuno.mkbs.lab1.services.FsWatcher;

public class Hacker {
    public static void main(String[] args) {
        File hackerFolder = new File(args[0]); // получаем путь к папкам из параметров запуска
        File publicFolder = new File(args[1]);

        if (!hackerFolder.exists() || !publicFolder.exists() // если одна их папок не существует/не является папкой то не запустится
            || !hackerFolder.isDirectory() || !publicFolder.isDirectory()) {
            System.out.println("Enter correct folders on startup!");
            return;
        }

        Thread watcherThread = new Thread(new FsWatcher(publicFolder, hackerFolder)); // если все ок то запускаем отслеживатель вдругом потоке

        watcherThread.start();

        System.out.println("Press any key to stop...");
        try {
            System.in.read(); // влючаем отслеживание нажатия - при нажатии завершаем программу
        } catch (Exception e) {
            e.printStackTrace();
        }

        watcherThread.interrupt();
    }
}