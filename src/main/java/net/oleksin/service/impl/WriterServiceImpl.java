package net.oleksin.service.impl;

import net.oleksin.service.WriterService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class WriterServiceImpl implements WriterService {
    @Override
    public void writeToFile(String data, String path) {
        final var file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            Files.write(file.toPath(), data.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
