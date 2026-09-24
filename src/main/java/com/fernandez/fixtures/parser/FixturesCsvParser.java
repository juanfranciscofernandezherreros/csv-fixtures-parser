package com.fernandez.fixtures.parser;

import com.fernandez.fixtures.dto.FixtureDTO;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

@Component
public class FixturesCsvParser {
    public void parseInChunks(Path path, int chunkSize, Consumer<List<FixtureDTO>> consumer) throws Exception {
        if (chunkSize <= 0) throw new IllegalArgumentException("chunkSize must be greater than zero");
        String[] metadata = metadata(path);
        try (Reader reader = Files.newBufferedReader(path);
             CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).setTrim(true).build().parse(reader)) {
            List<FixtureDTO> chunk = new ArrayList<>(chunkSize);
            for (CSVRecord record : parser) {
                if (record.size() == 1 && record.get(0).isBlank()) continue;
                if (record.size() != 4) {
                    throw new IllegalArgumentException("Invalid FIXTURES row " + record.getRecordNumber() + ": expected 4 columns");
                }
                chunk.add(new FixtureDTO(record.get(0), metadata[0], metadata[1], record.get(1), record.get(2), record.get(3)));
                if (chunk.size() == chunkSize) {
                    consumer.accept(List.copyOf(chunk));
                    chunk.clear();
                }
            }
            if (!chunk.isEmpty()) consumer.accept(List.copyOf(chunk));
        }
    }

    private String[] metadata(Path path) {
        String name = path.getFileName().toString().replaceFirst("(?i)\\.csv$", "");
        String[] parts = name.split("_");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Fixture filename must include country and competition");
        }
        return new String[]{parts[1], String.join("_", Arrays.copyOfRange(parts, 2, parts.length))};
    }
}
