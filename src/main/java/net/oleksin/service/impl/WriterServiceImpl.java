package net.oleksin.service.impl;

import net.oleksin.service.WriterService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WriterServiceImpl implements WriterService {
    @Override
    public void writeToFile(String data, String path) {
        final var filePath = Path.of(path);
        if (Files.isDirectory(filePath)) {
            throw new IllegalArgumentException("Path is a directory");
        }
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            Files.write(filePath, data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
