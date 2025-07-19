package com.shresthashreeson.RedisDemo.Integration;

import org.springframework.integration.file.filters.AbstractFileListFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class ContentHashFileListFilter extends AbstractFileListFilter<File> {

    private final Set<String> seenHashes = new ConcurrentSkipListSet<>();

    @Override
    public boolean accept(File file) {
        try {
            String hash = calculateFileHash(file);
            return seenHashes.add(hash); // Returns false if hash already seen
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String calculateFileHash(File file) throws IOException {
        MessageDigest digest;
        try (FileInputStream fis = new FileInputStream(file)) {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] byteArray = new byte[1024];
            int bytesCount;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        } catch (Exception e) {
            throw new IOException("Could not hash file: " + file.getName(), e);
        }

        byte[] bytes = digest.digest();
        return Base64.getEncoder().encodeToString(bytes); // Or Hex if preferred
    }
}
