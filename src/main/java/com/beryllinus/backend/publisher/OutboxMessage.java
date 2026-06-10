package com.beryllinus.backend.publisher;

import com.beryllinus.backend.enumuration.AggregateType;
import com.beryllinus.backend.enumuration.EventType;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OutboxMessage(

        UUID eventId,

        AggregateType aggregateType,

        UUID aggregateId,

        EventType eventType,

        String payload,

        OffsetDateTime createdAt
) {
}