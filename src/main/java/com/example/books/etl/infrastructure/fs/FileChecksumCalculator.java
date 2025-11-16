package com.example.books.etl.infrastructure.fs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileChecksumCalculator {

    private static final Logger LOG = LoggerFactory.getLogger(FileChecksumCalculator.class);
    private static final HexFormat HEX_FORMAT = HexFormat.of();

    public String sha256(Path path) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream inputStream = Files.newInputStream(path); DigestInputStream dis = new DigestInputStream(inputStream, digest)) {
                byte[] buffer = new byte[8192];
                while (dis.read(buffer) != -1) {
                    // consume stream fully
                }
            }
            return HEX_FORMAT.formatHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is not available", e);
        } catch (IOException e) {
            LOG.warn("Unable to calculate checksum for {}", path, e);
            return null;
        }
    }
}
