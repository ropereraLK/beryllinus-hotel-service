
package com.beryllinus.backend.service;

import com.beryllinus.backend.model.room.Room;
import com.beryllinus.backend.model.room.RoomClass;
import com.beryllinus.backend.model.room.RoomClassConfig;
import com.beryllinus.backend.model.room.RoomConfig;
import com.beryllinus.backend.model.room.RoomSetting;
import com.beryllinus.backend.repository.RoomClassConfigRepository;
import com.beryllinus.backend.repository.RoomClassRepository;
import com.beryllinus.backend.repository.RoomSettingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomSettingsServiceTest {

    @Mock
    private RoomService roomService;

    @Mock
    private RoomClassConfigRepository roomClassConfigRepository;

    @Mock
    private RoomClassRepository roomClassRepository;

    @Mock
    private RoomSettingRepository roomSettingRepository;

    @InjectMocks
    private RoomSettingService roomSettingService;

    @Test
    void shouldGenerateRoomSettingsForAllActiveRoomClasses() {

        // Business Scenario:
        // Two active RoomClasses exist.
        // One RoomSetting should be generated for each.

        LocalDate targetDate = LocalDate.of(2027, 6, 9);

        RoomClass standard = mock(RoomClass.class);
        RoomClass executive = mock(RoomClass.class);

        when(roomClassRepository.findAllByIsActive(true))
                .thenReturn(List.of(standard, executive));

        when(standard.getId()).thenReturn(1);
        when(executive.getId()).thenReturn(2);

        when(roomClassConfigRepository
                .findRoomsClassConfigByIdAndDateAndIsActive(anyInt(), eq(targetDate)))
                .thenReturn(Optional.empty());

        when(roomService.getAllActiveRoomsByRoomClass(any()))
                .thenReturn(Collections.emptyList());

        List<RoomSetting> result =
                roomSettingService.generateRoomSettingList(targetDate);

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveRoomClassesExist() {

        // Business Scenario:
        // No active RoomClasses exist.

        when(roomClassRepository.findAllByIsActive(true))
                .thenReturn(Collections.emptyList());

        List<RoomSetting> result =
                roomSettingService.generateRoomSettingList(LocalDate.now());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldUseBaseValuesWhenRoomClassConfigDoesNotExist() {

        LocalDate targetDate = LocalDate.of(2027, 6, 9);

        RoomClass roomClass = mock(RoomClass.class);

        when(roomClass.getId()).thenReturn(1);
        when(roomClass.isActive()).thenReturn(true);

        when(roomClass.getPriceLocal())
                .thenReturn(BigDecimal.valueOf(100));

        when(roomClass.getPriceInternational())
                .thenReturn(BigDecimal.valueOf(200));

        when(roomClassConfigRepository
                .findRoomsClassConfigByIdAndDateAndIsActive(1, targetDate))
                .thenReturn(Optional.empty());

        when(roomService.getAllActiveRoomsByRoomClass(roomClass))
                .thenReturn(Collections.emptyList());

        RoomSetting result =
                roomSettingService.generateRoomSettings(roomClass, targetDate);

        assertEquals(BigDecimal.valueOf(100),
                result.getCalcPriceLocal());

        assertEquals(BigDecimal.valueOf(200),
                result.getCalcPriceInternational());

        assertTrue(result.isCalculatedIsActive());
    }

    @Test
    void shouldOverrideBaseValuesWhenRoomClassConfigExists() {

        LocalDate targetDate = LocalDate.of(2027, 6, 9);

        RoomClass roomClass = mock(RoomClass.class);
        RoomClassConfig config = mock(RoomClassConfig.class);

        when(roomClass.getId()).thenReturn(1);

        when(config.isActive()).thenReturn(false);

        when(config.getPriceLocal())
                .thenReturn(BigDecimal.valueOf(150));

        when(config.getPriceInternational())
                .thenReturn(BigDecimal.valueOf(300));

        when(roomClassConfigRepository
                .findRoomsClassConfigByIdAndDateAndIsActive(1, targetDate))
                .thenReturn(Optional.of(List.of(config)));

        when(roomService.getAllActiveRoomsByRoomClass(roomClass))
                .thenReturn(Collections.emptyList());

        RoomSetting result =
                roomSettingService.generateRoomSettings(roomClass, targetDate);

        assertEquals(BigDecimal.valueOf(150),
                result.getCalcPriceLocal());

        assertEquals(BigDecimal.valueOf(300),
                result.getCalcPriceInternational());

        assertFalse(result.isCalculatedIsActive());
    }

    @Test
    void shouldCalculateAvailabilityWhenAllRoomsAvailable() {

        LocalDate targetDate = LocalDate.of(2027, 6, 9);

        RoomClass roomClass = mock(RoomClass.class);

        Room room1 = mock(Room.class);
        Room room2 = mock(Room.class);
        Room room3 = mock(Room.class);

        when(roomClass.getId()).thenReturn(1);

        when(roomClassConfigRepository
                .findRoomsClassConfigByIdAndDateAndIsActive(anyInt(), any()))
                .thenReturn(Optional.empty());

        when(roomService.getAllActiveRoomsByRoomClass(roomClass))
                .thenReturn(List.of(room1, room2, room3));

        when(roomService.getRoomConfigByDate(any(), eq(targetDate)))
                .thenReturn(Optional.empty());

        RoomSetting result =
                roomSettingService.generateRoomSettings(roomClass, targetDate);

        assertEquals(3, result.getAvailableRooms());
    }

    @Test
    void shouldCalculateAvailabilityWhenSomeRoomsUnavailable() {

        LocalDate targetDate = LocalDate.of(2027, 6, 9);

        RoomClass roomClass = mock(RoomClass.class);

        Room room1 = mock(Room.class);
        Room room2 = mock(Room.class);
        Room room3 = mock(Room.class);

        RoomConfig inactiveConfig = mock(RoomConfig.class);

        when(inactiveConfig.isActive()).thenReturn(false);

        when(roomClass.getId()).thenReturn(1);

        when(roomClassConfigRepository
                .findRoomsClassConfigByIdAndDateAndIsActive(anyInt(), any()))
                .thenReturn(Optional.empty());

        when(roomService.getAllActiveRoomsByRoomClass(roomClass))
                .thenReturn(List.of(room1, room2, room3));

        when(roomService.getRoomConfigByDate(room1, targetDate))
                .thenReturn(Optional.empty());

        when(roomService.getRoomConfigByDate(room2, targetDate))
                .thenReturn(Optional.empty());

        when(roomService.getRoomConfigByDate(room3, targetDate))
                .thenReturn(Optional.of(inactiveConfig));

        RoomSetting result =
                roomSettingService.generateRoomSettings(roomClass, targetDate);

        assertEquals(2, result.getAvailableRooms());
    }

    @Test
    void shouldPersistGeneratedRoomSettings() {

        LocalDate targetDate = LocalDate.of(2027, 6, 9);

        RoomClass roomClass = mock(RoomClass.class);

        when(roomClassRepository.findAllByIsActive(true))
                .thenReturn(List.of(roomClass));

        when(roomClass.getId()).thenReturn(1);

        when(roomClassConfigRepository
                .findRoomsClassConfigByIdAndDateAndIsActive(anyInt(), any()))
                .thenReturn(Optional.empty());

        when(roomService.getAllActiveRoomsByRoomClass(roomClass))
                .thenReturn(Collections.emptyList());

        when(roomSettingRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        roomSettingService.generateAndPersistRoomSettingList(targetDate);

        verify(roomSettingRepository)
                .saveAll(anyList());
    }
}
