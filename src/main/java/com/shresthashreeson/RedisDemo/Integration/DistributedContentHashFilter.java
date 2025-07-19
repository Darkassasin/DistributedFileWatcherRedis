package com.shresthashreeson.RedisDemo.Integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.file.filters.AbstractFileListFilter;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Base64;

public class DistributedContentHashFilter extends AbstractFileListFilter<File> {

    private final StringRedisTemplate redisTemplate;
    private final String redisKeyPrefix = "file-hash:";

    public DistributedContentHashFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean accept(File file) {
        try {
            String hash = calculateFileHash(file);
            String redisKey = redisKeyPrefix + hash;
            Boolean isNew = redisTemplate.opsForValue().setIfAbsent(redisKey, "1");

            return Boolean.TRUE.equals(isNew); // true if not seen before
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String calculateFileHash(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
        }
        byte[] hash = digest.digest();
        return Base64.getEncoder().encodeToString(hash);
    }
}

