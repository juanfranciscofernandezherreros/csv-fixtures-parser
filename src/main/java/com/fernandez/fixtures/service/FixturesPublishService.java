package com.fernandez.fixtures.service;

import com.fernandez.fixtures.avro.FixtureKey;
import com.fernandez.fixtures.avro.FixtureValue;
import com.fernandez.fixtures.error.NonRetryableCsvException;
import com.fernandez.fixtures.mapper.FixtureMessageMapper;
import com.fernandez.fixtures.parser.FixturesCsvParser;
import com.fernandez.fixtures.validation.SafeCsvPathValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@Service
public class FixturesPublishService {
    private static final int CHUNK_SIZE = 500;

    private final FixturesCsvParser parser;
    private final FixtureMessageMapper mapper;
    private final KafkaTemplate<FixtureKey, FixtureValue> kafka;
    private final SafeCsvPathValidator pathValidator;
    private final String topic;

    public FixturesPublishService(FixturesCsvParser parser, FixtureMessageMapper mapper,
                                  KafkaTemplate<FixtureKey, FixtureValue> kafka,
                                  SafeCsvPathValidator pathValidator,
                                  @Value("${app.kafka.topics.parsed-fixtures}") String topic) {
        this.parser = parser;
        this.mapper = mapper;
        this.kafka = kafka;
        this.pathValidator = pathValidator;
        this.topic = topic;
    }

    public void publish(String filePath) {
        try {
            Path csvPath = pathValidator.validate(filePath);
            parser.parseInChunks(csvPath, CHUNK_SIZE, rows -> {
                CompletableFuture<?>[] sends = rows.stream()
                        .map(row -> kafka.send(topic, mapper.key(row), mapper.value(row)))
                        .toArray(CompletableFuture[]::new);
                CompletableFuture.allOf(sends).join();
            });
        } catch (Exception exception) {
            if (containsKafkaFailure(exception)) {
                if (exception instanceof RuntimeException runtimeException) {
                    throw runtimeException;
                }
                throw new RuntimeException(exception);
            }
            throw new NonRetryableCsvException("Unable to parse FIXTURES CSV: " + filePath, exception);
        }
    }

    private boolean containsKafkaFailure(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof org.springframework.kafka.KafkaException
                    || current instanceof org.apache.kafka.common.KafkaException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
