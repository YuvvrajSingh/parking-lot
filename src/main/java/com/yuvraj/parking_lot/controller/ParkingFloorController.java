package com.yuvraj.parking_lot.controller;

import com.yuvraj.parking_lot.dto.ParkingFloorRequest;
import com.yuvraj.parking_lot.dto.ParkingFloorResponse;
import com.yuvraj.parking_lot.service.ParkingFloorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
@Tag(name = "Parking Floor", description = "Parking Floor management APIs")
public class ParkingFloorController {

    private final ParkingFloorService parkingFloorService;

    @PostMapping
    @Operation(summary = "Add a new floor to a parking lot")
    public ResponseEntity<ParkingFloorResponse> addFloor(@Valid @RequestBody ParkingFloorRequest request) {
        ParkingFloorResponse response = parkingFloorService.addFloor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get floor by ID")
    public ResponseEntity<ParkingFloorResponse> getFloorById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingFloorService.getFloorById(id));
    }

    @GetMapping("/parking-lot/{parkingLotId}")
    @Operation(summary = "Get all floors of a parking lot")
    public ResponseEntity<List<ParkingFloorResponse>> getFloorsByParkingLotId(@PathVariable Long parkingLotId) {
        return ResponseEntity.ok(parkingFloorService.getFloorsByParkingLotId(parkingLotId));
    }
}
