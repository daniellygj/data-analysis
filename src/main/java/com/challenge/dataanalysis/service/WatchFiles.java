package com.challenge.dataanalysis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

@Service
public class WatchFiles {

    private final FileService fileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(WatchFiles.class);

    private String pathInput;
    private String pathOutput;

    public WatchFiles(@Autowired FileService fileService) {
        this.fileService = fileService;
    }

    public void init(String folder) throws IOException, InterruptedException {
        pathInput = folder + "/data/in/";
        pathOutput = folder + "/data/out/";

        LOGGER.info("Starting watcher");

        this.readAlreadyExistentFiles();
        this.waitForNewFiles();
    }

    public void readAlreadyExistentFiles() {
        File path = new File(this.pathInput);
        File[] files = path.listFiles(file -> file.getName().endsWith(".dat"));

        if (files != null) {
            for (File file : files) {
                try {
                    LOGGER.info("File found");
                    fileService.readAndProcessFile(this.pathOutput, file);
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void waitForNewFiles() throws IOException, InterruptedException {
        Path folder = Paths.get(this.pathInput);

        WatchService watcher = folder.getFileSystem().newWatchService();
        folder.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

        while (true) {
            WatchKey key = watcher.take();

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == OVERFLOW) {
                    continue;
                }

                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                    String fileRead = this.pathInput + event.context().toString();
                    File file = new File(fileRead);
                    if (file.exists()) {
                        fileService.readAndProcessFile(this.pathOutput, file);
                        file.delete();
                    }

                }
            }

            if (!key.reset()) {
                break;
            }
        }
    }
}
