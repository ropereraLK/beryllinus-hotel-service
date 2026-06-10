package com.beryllinus.backend.publisher;

import com.beryllinus.backend.kafka.KafkaUtil;
import com.beryllinus.backend.model.OutboxEvent;
import com.beryllinus.backend.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaUtil kafkaUtil;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishEvents() {

        //Only 100 events are considered once and enough according to current stats
        List<OutboxEvent> events =
                outboxEventRepository.findPendingEvents(
                        PageRequest.of(0, 100));

        for (OutboxEvent event : events) {

            try {

                OutboxMessage message =
                        new OutboxMessage(
                                event.getId(),
                                event.getAggregateType(),
                                event.getAggregateId(),
                                event.getEventType(),
                                event.getPayload(),
                                event.getCreatedAt()
                        );

                kafkaTemplate.send(
                        kafkaUtil.resolveTopic(event),
                        event.getAggregateId().toString(),
                        objectMapper.writeValueAsString(message)
                );

                event.setPublished(true);

                log.info(
                        "Published event {}",
                        event.getId());

            } catch (Exception ex) {

                log.error(
                        "Failed publishing event {}",
                        event.getId(),
                        ex);

                break;
            }
        }
    }
}
