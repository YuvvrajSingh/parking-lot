package com.yuvraj.parking_lot.service.impl;

import com.yuvraj.parking_lot.dto.ParkingSpotRequest;
import com.yuvraj.parking_lot.dto.ParkingSpotResponse;
import com.yuvraj.parking_lot.entity.ParkingFloor;
import com.yuvraj.parking_lot.entity.ParkingSpot;
import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import com.yuvraj.parking_lot.exception.ResourceNotFoundException;
import com.yuvraj.parking_lot.repository.ParkingFloorRepository;
import com.yuvraj.parking_lot.repository.ParkingSpotRepository;
import com.yuvraj.parking_lot.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingFloorRepository parkingFloorRepository;

    @Override
    public ParkingSpotResponse addSpot(ParkingSpotRequest request) {
        ParkingFloor floor = parkingFloorRepository.findById(request.getFloorId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking floor not found with id: " + request.getFloorId()));

        ParkingSpot spot = ParkingSpot.builder()
                .spotNumber(request.getSpotNumber())
                .type(request.getType())
                .isOccupied(false)
                .floor(floor)
                .build();

        ParkingSpot saved = parkingSpotRepository.save(spot);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ParkingSpotResponse getSpotById(Long id) {
        ParkingSpot spot = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking spot not found with id: " + id));
        return mapToResponse(spot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkingSpotResponse> getAvailableSpotsByType(ParkingSpotType type) {
        return parkingSpotRepository.findByTypeAndIsOccupiedFalse(type)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkingSpotResponse> getSpotsByFloorId(Long floorId) {
        return parkingSpotRepository.findByFloorId(floorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ParkingSpotResponse mapToResponse(ParkingSpot spot) {
        return ParkingSpotResponse.builder()
                .id(spot.getId())
                .spotNumber(spot.getSpotNumber())
                .type(spot.getType())
                .isOccupied(spot.getIsOccupied())
                .floorId(spot.getFloor().getId())
                .floorNumber(spot.getFloor().getFloorNumber())
                .build();
    }
}
