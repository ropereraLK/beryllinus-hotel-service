package com.beryllinus.backend.kafka;

import com.beryllinus.backend.configuration.KafkaTopicConfiguration;
import com.beryllinus.backend.model.OutboxEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUtil {

    private final KafkaTopicConfiguration topics;

    public String resolveTopic(OutboxEvent event) {

        return switch (event.getAggregateType()) {

            case BOOKING -> topics.booking();
            case PAYMENT -> topics.payment();
            case INVENTORY -> topics.inventory();
        };
    }
}
