package com.fernandez.fixtures.config;

import com.fernandez.fixtures.error.NonRetryableCsvException;
import org.apache.kafka.common.errors.TimeoutException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaErrorClassifierTest {

    private final KafkaErrorClassifier classifier = new KafkaErrorClassifier();

    @Test
    void treatsCsvDataFailureAsNonRetryable() {
        assertThat(classifier.isRetryable(
                new NonRetryableCsvException("invalid csv", new IllegalArgumentException("bad row"))))
                .isFalse();
    }

    @Test
    void treatsTransientKafkaFailureAsRetryable() {
        assertThat(classifier.isRetryable(new TimeoutException("broker unavailable"))).isTrue();
    }
}
