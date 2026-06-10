package com.beryllinus.backend.repository;

import com.beryllinus.backend.model.OutboxEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("""
             select oe
             from OutboxEvent oe
             where oe.published = false
             order by oe.createdAt asc
            """)
    List<OutboxEvent> findPendingEvents(
            Pageable pageable);
}
