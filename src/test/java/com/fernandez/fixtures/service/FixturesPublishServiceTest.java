package com.fernandez.fixtures.service;

import com.fernandez.fixtures.avro.FixtureKey;
import com.fernandez.fixtures.avro.FixtureValue;
import com.fernandez.fixtures.dto.FixtureDTO;
import com.fernandez.fixtures.mapper.FixtureMessageMapper;
import com.fernandez.fixtures.parser.FixturesCsvParser;
import com.fernandez.fixtures.validation.SafeCsvPathValidator;
import org.apache.kafka.common.KafkaException;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FixturesPublishServiceTest {

    @Test
    void sendsWholeChunkBeforeWaitingForFirstAck() throws Exception {
        FixturesCsvParser parser = mock(FixturesCsvParser.class);
        FixtureMessageMapper mapper = mock(FixtureMessageMapper.class);
        @SuppressWarnings("unchecked")
        KafkaTemplate<FixtureKey, FixtureValue> kafka = mock(KafkaTemplate.class);
        SafeCsvPathValidator pathValidator = mock(SafeCsvPathValidator.class);
        FixtureDTO first = mock(FixtureDTO.class);
        FixtureDTO second = mock(FixtureDTO.class);
        FixtureKey key = mock(FixtureKey.class);
        FixtureValue value = mock(FixtureValue.class);
        Path path = Path.of("/data/fixtures.csv");

        when(pathValidator.validate("/data/fixtures.csv")).thenReturn(path);
        doAnswer(call -> {
            @SuppressWarnings("unchecked")
            java.util.function.Consumer<List<FixtureDTO>> consumer = call.getArgument(2);
            consumer.accept(List.of(first, second));
            return null;
        }).when(parser).parseInChunks(eq(path), eq(500), any());

        when(mapper.key(any())).thenReturn(key);
        when(mapper.value(any())).thenReturn(value);

        CompletableFuture<Object> firstAck = new CompletableFuture<>();
        CountDownLatch secondSent = new CountDownLatch(1);
        java.util.concurrent.atomic.AtomicInteger sends = new java.util.concurrent.atomic.AtomicInteger();
        when(kafka.send("fixtures.parsed", key, value)).thenAnswer(invocation -> {
            if (sends.incrementAndGet() == 1) return firstAck;
            secondSent.countDown();
            return CompletableFuture.completedFuture(null);
        });

        FixturesPublishService service =
                new FixturesPublishService(parser, mapper, kafka, pathValidator, "fixtures.parsed");
        var executor = Executors.newSingleThreadExecutor();
        try {
            var publish = executor.submit(() -> service.publish("/data/fixtures.csv"));
            assertTrue(secondSent.await(1, TimeUnit.SECONDS));
            firstAck.complete(null);
            publish.get(2, TimeUnit.SECONDS);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void failedAckFailsProcessing() throws Exception {
        FixturesCsvParser parser = mock(FixturesCsvParser.class);
        FixtureMessageMapper mapper = mock(FixtureMessageMapper.class);
        @SuppressWarnings("unchecked")
        KafkaTemplate<FixtureKey, FixtureValue> kafka = mock(KafkaTemplate.class);
        SafeCsvPathValidator pathValidator = mock(SafeCsvPathValidator.class);
        FixtureDTO row = mock(FixtureDTO.class);
        FixtureKey key = mock(FixtureKey.class);
        FixtureValue value = mock(FixtureValue.class);
        Path path = Path.of("/data/fixtures.csv");

        when(pathValidator.validate("/data/fixtures.csv")).thenReturn(path);
        doAnswer(call -> {
            @SuppressWarnings("unchecked")
            java.util.function.Consumer<List<FixtureDTO>> consumer = call.getArgument(2);
            consumer.accept(List.of(row));
            return null;
        }).when(parser).parseInChunks(eq(path), eq(500), any());

        when(mapper.key(any())).thenReturn(key);
        when(mapper.value(any())).thenReturn(value);
        when(kafka.send(any(), any(), any()))
                .thenReturn((CompletableFuture) CompletableFuture.failedFuture(new KafkaException("down")));

        FixturesPublishService service =
                new FixturesPublishService(parser, mapper, kafka, pathValidator, "fixtures.parsed");
        assertThrows(RuntimeException.class, () -> service.publish("/data/fixtures.csv"));
    }
}
