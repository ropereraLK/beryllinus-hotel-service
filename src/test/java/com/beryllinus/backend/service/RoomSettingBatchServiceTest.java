package com.beryllinus.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomSettingBatchServiceTest {

    @Mock
    private RoomSettingService roomSettingService;

    private RoomSettingBatchService service;

    @Test
    void shouldGenerateSettingsForNextYearOnNormalDay() {

        // -----------------------------------------------------
        // Business Scenario
        // -----------------------------------------------------
        // Today's date: 2026-06-09
        //
        // Requirement:
        // Generate RoomSettings exactly one year ahead.
        //
        // Expected:
        // RoomSettings should be generated for:
        // 2027-06-09
        // -----------------------------------------------------

        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-06-09T00:00:00Z"),
                ZoneOffset.UTC
        );

        service = new RoomSettingBatchService(
                roomSettingService,
                fixedClock
        );

        when(roomSettingService.generateAndPersistRoomSettingList(any()))
                .thenReturn(Collections.emptyList());

        service.createRoomSettingsAsBatch();

        verify(roomSettingService)
                .generateAndPersistRoomSettingList(
                        LocalDate.of(2027, 6, 9)
                );
    }

    @Test
    void shouldSkipOnFeb29() {

        // -----------------------------------------------------
        // Business Scenario
        // -----------------------------------------------------
        // Today's date: 2028-02-29
        //
        // Requirement:
        // Leap day must be ignored because RoomSettings for
        // this date would already have been generated on
        // 2027-02-28.
        //
        // Expected:
        // No RoomSettings should be generated.
        // -----------------------------------------------------

        Clock fixedClock = Clock.fixed(
                Instant.parse("2028-02-29T00:00:00Z"),
                ZoneOffset.UTC
        );

        service = new RoomSettingBatchService(
                roomSettingService,
                fixedClock
        );

        service.createRoomSettingsAsBatch();

        verifyNoInteractions(roomSettingService);
    }

    @Test
    void shouldGenerateFeb28AndFeb29ForNextLeapYear() {

        // -----------------------------------------------------
        // Business Scenario
        // -----------------------------------------------------
        // Today's date: 2027-02-28
        //
        // Next year (2028) is a leap year.
        //
        // Requirement:
        // Generate RoomSettings for:
        // - 2028-02-28
        // - 2028-02-29
        //
        // Expected:
        // Service should be called twice.
        // -----------------------------------------------------

        Clock fixedClock = Clock.fixed(
                Instant.parse("2027-02-28T00:00:00Z"),
                ZoneOffset.UTC
        );

        service = new RoomSettingBatchService(
                roomSettingService,
                fixedClock
        );

        when(roomSettingService.generateAndPersistRoomSettingList(any()))
                .thenReturn(Collections.emptyList());

        service.createRoomSettingsAsBatch();

        verify(roomSettingService)
                .generateAndPersistRoomSettingList(
                        LocalDate.of(2028, 2, 28)
                );

        verify(roomSettingService)
                .generateAndPersistRoomSettingList(
                        LocalDate.of(2028, 2, 29)
                );

        verify(roomSettingService, times(2))
                .generateAndPersistRoomSettingList(any());
    }

    @Test
    void shouldGenerateOnlyFeb28WhenNextYearIsNotLeapYear() {

        // -----------------------------------------------------
        // Business Scenario
        // -----------------------------------------------------
        // Today's date: 2026-02-28
        //
        // Next year (2027) is NOT a leap year.
        //
        // Requirement:
        // Generate RoomSettings only for:
        // - 2027-02-28
        //
        // Expected:
        // Service should be called exactly once.
        // -----------------------------------------------------

        Clock fixedClock = Clock.fixed(
                Instant.parse("2026-02-28T00:00:00Z"),
                ZoneOffset.UTC
        );

        service = new RoomSettingBatchService(
                roomSettingService,
                fixedClock
        );

        when(roomSettingService.generateAndPersistRoomSettingList(any()))
                .thenReturn(Collections.emptyList());

        service.createRoomSettingsAsBatch();

        verify(roomSettingService)
                .generateAndPersistRoomSettingList(
                        LocalDate.of(2027, 2, 28)
                );

        verify(roomSettingService, times(1))
                .generateAndPersistRoomSettingList(any());
    }
}
