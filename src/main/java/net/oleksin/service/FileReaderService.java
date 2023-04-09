package net.oleksin.service;

import net.oleksin.service.ReaderService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReaderService implements ReaderService {
    @Override
    public List<String> readFromFile(String fileName) {
        final var path = Path.of(fileName);
        if (Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path is a directory");
        }
        if (!Files.exists(path)) {
            return List.of();
        }
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
