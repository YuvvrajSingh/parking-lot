package com.yuvraj.parking_lot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuvraj.parking_lot.dto.ParkRequest;
import com.yuvraj.parking_lot.entity.ParkingFloor;
import com.yuvraj.parking_lot.entity.ParkingLot;
import com.yuvraj.parking_lot.entity.ParkingSpot;
import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import com.yuvraj.parking_lot.entity.enums.VehicleType;
import com.yuvraj.parking_lot.repository.ParkingFloorRepository;
import com.yuvraj.parking_lot.repository.ParkingLotRepository;
import com.yuvraj.parking_lot.repository.ParkingSpotRepository;
import com.yuvraj.parking_lot.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VehicleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingFloorRepository parkingFloorRepository;

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
        parkingSpotRepository.deleteAll();
        parkingFloorRepository.deleteAll();
        parkingLotRepository.deleteAll();

        ParkingLot lot = parkingLotRepository.save(ParkingLot.builder()
                .name("Test Lot").address("Test Address").build());

        ParkingFloor floor = parkingFloorRepository.save(ParkingFloor.builder()
                .floorNumber(1).parkingLot(lot).build());

        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("C-1").type(ParkingSpotType.COMPACT).isOccupied(false).floor(floor).build());
        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("E-1").type(ParkingSpotType.ELECTRIC).isOccupied(false).floor(floor).build());
    }

    @Test
    @DisplayName("POST /api/vehicles/park - should park vehicle")
    void testParkVehicle() throws Exception {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("INT-001")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        mockMvc.perform(post("/api/vehicles/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.licensePlate").value("INT-001"))
                .andExpect(jsonPath("$.vehicleType").value("CAR"))
                .andExpect(jsonPath("$.spotType").value("COMPACT"))
                .andExpect(jsonPath("$.entryTime").isNotEmpty());
    }

    @Test
    @DisplayName("POST /api/vehicles/exit/{licensePlate} - should exit vehicle")
    void testExitVehicle() throws Exception {
        // First park
        ParkRequest request = ParkRequest.builder()
                .licensePlate("INT-002")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        mockMvc.perform(post("/api/vehicles/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Then exit
        mockMvc.perform(post("/api/vehicles/exit/INT-002"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licensePlate").value("INT-002"))
                .andExpect(jsonPath("$.exitTime").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/vehicles/availability - should return availability")
    void testGetAvailability() throws Exception {
        mockMvc.perform(get("/api/vehicles/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAvailable").value(2))
                .andExpect(jsonPath("$.availableSpots.COMPACT").value(1))
                .andExpect(jsonPath("$.availableSpots.ELECTRIC").value(1));
    }

    @Test
    @DisplayName("POST /api/vehicles/park - should return 409 for duplicate vehicle")
    void testParkDuplicateVehicle() throws Exception {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("DUP-001")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        mockMvc.perform(post("/api/vehicles/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/vehicles/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST /api/vehicles/exit - should return 404 for non-existent vehicle")
    void testExitNonExistentVehicle() throws Exception {
        mockMvc.perform(post("/api/vehicles/exit/NON-EXIST"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/vehicles/park - should return 400 for invalid request")
    void testParkVehicleValidation() throws Exception {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("")
                .build();

        mockMvc.perform(post("/api/vehicles/park")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
