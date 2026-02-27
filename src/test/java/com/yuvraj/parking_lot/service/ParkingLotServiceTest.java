package com.yuvraj.parking_lot.service;

import com.yuvraj.parking_lot.dto.ParkingLotRequest;
import com.yuvraj.parking_lot.dto.ParkingLotResponse;
import com.yuvraj.parking_lot.exception.ResourceNotFoundException;
import com.yuvraj.parking_lot.repository.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ParkingLotServiceTest {

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    void setUp() {
        parkingLotRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create parking lot successfully")
    void testCreateParkingLot() {
        ParkingLotRequest request = ParkingLotRequest.builder()
                .name("City Mall Parking")
                .address("100 Main St")
                .build();

        ParkingLotResponse response = parkingLotService.createParkingLot(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("City Mall Parking", response.getName());
        assertEquals("100 Main St", response.getAddress());
        assertEquals(0, response.getTotalFloors());
    }

    @Test
    @DisplayName("Should get parking lot by ID")
    void testGetParkingLotById() {
        ParkingLotRequest request = ParkingLotRequest.builder()
                .name("Airport Parking")
                .address("Airport Rd")
                .build();

        ParkingLotResponse created = parkingLotService.createParkingLot(request);
        ParkingLotResponse found = parkingLotService.getParkingLotById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals("Airport Parking", found.getName());
    }

    @Test
    @DisplayName("Should throw exception for non-existent parking lot")
    void testGetParkingLotByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> parkingLotService.getParkingLotById(999L));
    }

    @Test
    @DisplayName("Should get all parking lots")
    void testGetAllParkingLots() {
        parkingLotService.createParkingLot(ParkingLotRequest.builder()
                .name("Lot A").address("Address A").build());
        parkingLotService.createParkingLot(ParkingLotRequest.builder()
                .name("Lot B").address("Address B").build());

        List<ParkingLotResponse> lots = parkingLotService.getAllParkingLots();

        assertEquals(2, lots.size());
    }
}
