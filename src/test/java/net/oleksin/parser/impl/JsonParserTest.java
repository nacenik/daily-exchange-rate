package net.oleksin.parser.impl;

import com.google.gson.JsonSyntaxException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonParserTest {
    private static String json;
    private static JsonParser jsonParser;

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        jsonParser = new JsonParser();
        json = Files.readAllLines(Path.of("src/test/resources/test.json"))
                .stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    @Test
    void parse_json_ok() {
        final var observationsBySeriesData = assertDoesNotThrow(() -> jsonParser.parse(json));
        assertNotNull(observationsBySeriesData);
        assertNotNull(observationsBySeriesData.getObservations());
    }

    @Test
    void parse_json_throwJsonSyntaxException() {
        assertThrows(JsonSyntaxException.class, () -> jsonParser.parse("not json"));
    }
}