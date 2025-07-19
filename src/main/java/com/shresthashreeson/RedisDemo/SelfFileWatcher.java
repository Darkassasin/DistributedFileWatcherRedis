package com.shresthashreeson.RedisDemo;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

//@Component
@Deprecated
public class SelfFileWatcher {

    private static final String DIRECTORY_TO_WATCH = System.getenv("WATCH_DIRECTORY");

//    @PostConstruct
    public void watchDirectory() {
        Thread watcherThread = new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(DIRECTORY_TO_WATCH);

                path.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE);

                System.out.println("Watching directory: " + DIRECTORY_TO_WATCH);

                while (true) {
                    WatchKey key = watchService.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path filePath = (Path) event.context();

                        System.out.printf("Event kind: %s, File affected: %s%n", kind, filePath);
                        // You can add further file processing logic here
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        watcherThread.setDaemon(true);
        watcherThread.setName("FileWatcherThread");
        watcherThread.start();
    }
}

