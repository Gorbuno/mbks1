package gorbuno.mkbs.lab1.utils;

import java.io.*;
import java.nio.file.StandardWatchEventKinds;

public interface FsListening {
    void processFsEvent(StandardWatchEventKinds kind, File changedFile);
}
