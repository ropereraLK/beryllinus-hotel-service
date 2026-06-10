package com.beryllinus.backend.model;

import com.beryllinus.backend.enumuration.AggregateType;
import com.beryllinus.backend.enumuration.EventType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
@Entity
@Table(
        name = "outbox_events",
        indexes = {
                @Index(
                        name = "idx_outbox_published_created",
                        columnList = "published, createdAt"
                ),
                @Index(
                        name = "idx_outbox_aggregate",
                        columnList = "aggregateType, aggregateId"
                ),
                @Index(
                        name = "idx_outbox_publish_retry",
                        columnList = "published, retryCount, createdAt"
                )
        }
)
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;

    private UUID aggregateId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Lob
    private String payload;

    private OffsetDateTime createdAt;

    private boolean published;

    private OffsetDateTime publishedAt;

    private int retryCount;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }


}