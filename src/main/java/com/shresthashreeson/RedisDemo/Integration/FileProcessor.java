package com.shresthashreeson.RedisDemo.Integration;

import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class FileProcessor {

    public void process(File file) {
        if(file.getAbsolutePath().contains(".swp")) {
            return; // Skip .swp files
        }
        System.out.println("📂 Processing file: " + file.getAbsolutePath());

        // Add your file handling logic here, e.g.:
        // - Read and parse file
        // - Move to processed folder
        // - Notify another service
    }
}
