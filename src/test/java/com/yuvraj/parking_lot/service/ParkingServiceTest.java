package com.yuvraj.parking_lot.service;

import com.yuvraj.parking_lot.dto.AvailabilityResponse;
import com.yuvraj.parking_lot.dto.ParkRequest;
import com.yuvraj.parking_lot.dto.VehicleResponse;
import com.yuvraj.parking_lot.entity.ParkingFloor;
import com.yuvraj.parking_lot.entity.ParkingLot;
import com.yuvraj.parking_lot.entity.ParkingSpot;
import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import com.yuvraj.parking_lot.entity.enums.VehicleType;
import com.yuvraj.parking_lot.exception.SpotNotAvailableException;
import com.yuvraj.parking_lot.exception.VehicleAlreadyParkedException;
import com.yuvraj.parking_lot.exception.VehicleNotFoundException;
import com.yuvraj.parking_lot.repository.ParkingFloorRepository;
import com.yuvraj.parking_lot.repository.ParkingLotRepository;
import com.yuvraj.parking_lot.repository.ParkingSpotRepository;
import com.yuvraj.parking_lot.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParkingServiceTest {

    @Autowired
    private ParkingService parkingService;

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

        // Create a parking lot with a floor and spots
        ParkingLot lot = ParkingLot.builder()
                .name("Test Lot")
                .address("123 Test St")
                .build();
        lot = parkingLotRepository.save(lot);

        ParkingFloor floor = ParkingFloor.builder()
                .floorNumber(1)
                .parkingLot(lot)
                .build();
        floor = parkingFloorRepository.save(floor);

        // Add various spots
        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("C-1").type(ParkingSpotType.COMPACT).isOccupied(false).floor(floor).build());
        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("C-2").type(ParkingSpotType.COMPACT).isOccupied(false).floor(floor).build());
        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("L-1").type(ParkingSpotType.LARGE).isOccupied(false).floor(floor).build());
        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("M-1").type(ParkingSpotType.MOTORBIKE).isOccupied(false).floor(floor).build());
        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("E-1").type(ParkingSpotType.ELECTRIC).isOccupied(false).floor(floor).build());
        parkingSpotRepository.save(ParkingSpot.builder()
                .spotNumber("H-1").type(ParkingSpotType.HANDICAPPED).isOccupied(false).floor(floor).build());
    }

    @Test
    @DisplayName("Should park vehicle successfully")
    void testParkVehicleSuccess() {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("ABC-1234")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        VehicleResponse response = parkingService.parkVehicle(request);

        assertNotNull(response);
        assertEquals("ABC-1234", response.getLicensePlate());
        assertEquals(VehicleType.CAR, response.getVehicleType());
        assertNotNull(response.getEntryTime());
        assertNull(response.getExitTime());
        assertNotNull(response.getSpotNumber());
        assertEquals("COMPACT", response.getSpotType());
    }

    @Test
    @DisplayName("Should throw exception when vehicle is already parked")
    void testParkVehicleAlreadyParked() {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("ABC-1234")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        parkingService.parkVehicle(request);

        assertThrows(VehicleAlreadyParkedException.class, () -> parkingService.parkVehicle(request));
    }

    @Test
    @DisplayName("Should throw exception when no spot is available")
    void testParkVehicleNoSpotAvailable() {
        // Park in the only motorbike spot
        ParkRequest request1 = ParkRequest.builder()
                .licensePlate("MOTO-001")
                .vehicleType(VehicleType.MOTORBIKE)
                .spotType(ParkingSpotType.MOTORBIKE)
                .build();
        parkingService.parkVehicle(request1);

        // Try to park another motorbike
        ParkRequest request2 = ParkRequest.builder()
                .licensePlate("MOTO-002")
                .vehicleType(VehicleType.MOTORBIKE)
                .spotType(ParkingSpotType.MOTORBIKE)
                .build();

        assertThrows(SpotNotAvailableException.class, () -> parkingService.parkVehicle(request2));
    }

    @Test
    @DisplayName("Should exit vehicle successfully")
    void testExitVehicleSuccess() {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("EXIT-001")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        parkingService.parkVehicle(request);

        VehicleResponse response = parkingService.exitVehicle("EXIT-001");

        assertNotNull(response);
        assertEquals("EXIT-001", response.getLicensePlate());
        assertNotNull(response.getEntryTime());
        assertNotNull(response.getExitTime());
    }

    @Test
    @DisplayName("Should throw exception when exiting non-existing vehicle")
    void testExitVehicleNotFound() {
        assertThrows(VehicleNotFoundException.class, () -> parkingService.exitVehicle("NON-EXIST"));
    }

    @Test
    @DisplayName("Should return correct availability counts")
    void testGetAvailableSpots() {
        AvailabilityResponse response = parkingService.getAvailableSpots();

        assertNotNull(response);
        assertEquals(6, response.getTotalAvailable());
        assertEquals(2L, response.getAvailableSpots().get(ParkingSpotType.COMPACT));
        assertEquals(1L, response.getAvailableSpots().get(ParkingSpotType.LARGE));
        assertEquals(1L, response.getAvailableSpots().get(ParkingSpotType.MOTORBIKE));
        assertEquals(1L, response.getAvailableSpots().get(ParkingSpotType.ELECTRIC));
        assertEquals(1L, response.getAvailableSpots().get(ParkingSpotType.HANDICAPPED));
    }

    @Test
    @DisplayName("Should decrement availability after parking")
    void testAvailabilityDecrementsAfterParking() {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("DEC-001")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        parkingService.parkVehicle(request);

        AvailabilityResponse response = parkingService.getAvailableSpots();
        assertEquals(1L, response.getAvailableSpots().get(ParkingSpotType.COMPACT));
        assertEquals(5, response.getTotalAvailable());
    }

    @Test
    @DisplayName("Should increment availability after exit")
    void testAvailabilityIncrementsAfterExit() {
        ParkRequest request = ParkRequest.builder()
                .licensePlate("INC-001")
                .vehicleType(VehicleType.CAR)
                .spotType(ParkingSpotType.COMPACT)
                .build();

        parkingService.parkVehicle(request);
        parkingService.exitVehicle("INC-001");

        AvailabilityResponse response = parkingService.getAvailableSpots();
        assertEquals(2L, response.getAvailableSpots().get(ParkingSpotType.COMPACT));
        assertEquals(6, response.getTotalAvailable());
    }
}
