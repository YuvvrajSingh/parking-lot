package com.yuvraj.parking_lot.controller;

import com.yuvraj.parking_lot.dto.AvailabilityResponse;
import com.yuvraj.parking_lot.dto.ParkRequest;
import com.yuvraj.parking_lot.dto.VehicleResponse;
import com.yuvraj.parking_lot.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Parking", description = "Vehicle park and exit APIs")
public class VehicleController {

    private final ParkingService parkingService;

    @PostMapping("/park")
    @Operation(summary = "Park a vehicle")
    public ResponseEntity<VehicleResponse> parkVehicle(@Valid @RequestBody ParkRequest request) {
        VehicleResponse response = parkingService.parkVehicle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/exit/{licensePlate}")
    @Operation(summary = "Exit a vehicle from parking")
    public ResponseEntity<VehicleResponse> exitVehicle(@PathVariable String licensePlate) {
        return ResponseEntity.ok(parkingService.exitVehicle(licensePlate));
    }

    @GetMapping("/availability")
    @Operation(summary = "Get real-time parking availability")
    public ResponseEntity<AvailabilityResponse> getAvailability() {
        return ResponseEntity.ok(parkingService.getAvailableSpots());
    }
}
