package com.yuvraj.parking_lot.controller;

import com.yuvraj.parking_lot.dto.ParkingSpotRequest;
import com.yuvraj.parking_lot.dto.ParkingSpotResponse;
import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import com.yuvraj.parking_lot.service.ParkingSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
@Tag(name = "Parking Spot", description = "Parking Spot management APIs")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @PostMapping
    @Operation(summary = "Add a new parking spot to a floor")
    public ResponseEntity<ParkingSpotResponse> addSpot(@Valid @RequestBody ParkingSpotRequest request) {
        ParkingSpotResponse response = parkingSpotService.addSpot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get parking spot by ID")
    public ResponseEntity<ParkingSpotResponse> getSpotById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingSpotService.getSpotById(id));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available spots by type")
    public ResponseEntity<List<ParkingSpotResponse>> getAvailableSpots(@RequestParam ParkingSpotType type) {
        return ResponseEntity.ok(parkingSpotService.getAvailableSpotsByType(type));
    }

    @GetMapping("/floor/{floorId}")
    @Operation(summary = "Get all spots on a floor")
    public ResponseEntity<List<ParkingSpotResponse>> getSpotsByFloor(@PathVariable Long floorId) {
        return ResponseEntity.ok(parkingSpotService.getSpotsByFloorId(floorId));
    }
}
