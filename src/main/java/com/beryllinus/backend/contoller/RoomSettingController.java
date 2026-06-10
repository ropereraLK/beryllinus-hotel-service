package com.beryllinus.backend.contoller;

import com.beryllinus.backend.model.room.RoomSetting;
import com.beryllinus.backend.enumuration.RoomClassType;
import com.beryllinus.backend.service.RoomSettingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/room-settings")
public class RoomSettingController {

    private final RoomSettingService roomSettingService;

    public RoomSettingController(
            RoomSettingService roomSettingService
    ) {
        this.roomSettingService = roomSettingService;
    }

    /**
     * Get all RoomSettings for a specific date.
     * <p>
     * Example:
     * GET /api/v1/room-settings/date/2026-06-09
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<List<RoomSetting>> getByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {

        return ResponseEntity.ok(
                roomSettingService.getByDate(date)
        );
    }

    /**
     * Get RoomSettings within a date range.
     * <p>
     * Example:
     * GET /api/v1/room-settings/date-range?startDate=2026-06-09&endDate=2026-06-15
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<RoomSetting>> getByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        return ResponseEntity.ok(
                roomSettingService.getByDateRange(
                        startDate,
                        endDate
                )
        );
    }

    /**
     * Get RoomSetting for a RoomClass on a specific date.
     * <p>
     * Example:
     * GET /api/v1/room-settings/DELUXE/date/2026-06-09
     */
    @GetMapping("/{roomClass}/date/{date}")
    public ResponseEntity<RoomSetting> getByRoomClassAndDate(
            @PathVariable RoomClassType roomClass,

            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {

        return ResponseEntity.ok(
                roomSettingService.getByRoomClassAndDate(
                        roomClass,
                        date
                )
        );
    }

    /**
     * Get RoomSettings for a RoomClass within a date range.
     * <p>
     * Example:
     * GET /api/v1/room-settings/DELUXE?startDate=2026-06-09&endDate=2026-06-15
     */
    @GetMapping("/{roomClass}")
    public ResponseEntity<List<RoomSetting>> getByRoomClassAndDateRange(
            @PathVariable RoomClassType roomClass,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        return ResponseEntity.ok(
                roomSettingService.getByRoomClassAndDateRange(
                        roomClass,
                        startDate,
                        endDate
                )
        );
    }
}