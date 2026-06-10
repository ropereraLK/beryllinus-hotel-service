package com.beryllinus.backend.service;

import com.beryllinus.backend.model.OutboxEvent;
import com.beryllinus.backend.repository.OutboxEventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;

    public OutboxEvent addOutboxEvent(OutboxEvent outboxEvent) {
        return outboxEventRepository.save(outboxEvent);
    }

}
