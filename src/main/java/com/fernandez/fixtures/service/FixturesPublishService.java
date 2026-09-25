package com.fernandez.fixtures.service;

import com.fernandez.fixtures.avro.FixtureKey;
import com.fernandez.fixtures.avro.FixtureValue;
import com.fernandez.fixtures.mapper.FixtureMessageMapper;
import com.fernandez.fixtures.parser.FixturesCsvParser;
import com.fernandez.fixtures.validation.SafeCsvPathValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

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
            parser.parseInChunks(csvPath, CHUNK_SIZE,
                    rows -> rows.forEach(row -> kafka.send(topic, mapper.key(row), mapper.value(row)).join()));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to parse FIXTURES CSV: " + filePath, exception);
        }
    }
}
