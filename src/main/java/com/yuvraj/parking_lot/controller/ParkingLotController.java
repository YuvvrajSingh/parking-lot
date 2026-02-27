package com.yuvraj.parking_lot.controller;

import com.yuvraj.parking_lot.dto.ParkingLotRequest;
import com.yuvraj.parking_lot.dto.ParkingLotResponse;
import com.yuvraj.parking_lot.service.ParkingLotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
@RequiredArgsConstructor
@Tag(name = "Parking Lot", description = "Parking Lot management APIs")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @PostMapping
    @Operation(summary = "Create a new parking lot")
    public ResponseEntity<ParkingLotResponse> createParkingLot(@Valid @RequestBody ParkingLotRequest request) {
        ParkingLotResponse response = parkingLotService.createParkingLot(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get parking lot by ID")
    public ResponseEntity<ParkingLotResponse> getParkingLotById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingLotService.getParkingLotById(id));
    }

    @GetMapping
    @Operation(summary = "Get all parking lots")
    public ResponseEntity<List<ParkingLotResponse>> getAllParkingLots() {
        return ResponseEntity.ok(parkingLotService.getAllParkingLots());
    }
}
