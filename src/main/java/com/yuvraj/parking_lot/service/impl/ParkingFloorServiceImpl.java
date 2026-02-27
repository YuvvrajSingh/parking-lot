package com.yuvraj.parking_lot.service.impl;

import com.yuvraj.parking_lot.dto.ParkingFloorRequest;
import com.yuvraj.parking_lot.dto.ParkingFloorResponse;
import com.yuvraj.parking_lot.entity.ParkingFloor;
import com.yuvraj.parking_lot.entity.ParkingLot;
import com.yuvraj.parking_lot.exception.ResourceNotFoundException;
import com.yuvraj.parking_lot.repository.ParkingFloorRepository;
import com.yuvraj.parking_lot.repository.ParkingLotRepository;
import com.yuvraj.parking_lot.service.ParkingFloorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingFloorServiceImpl implements ParkingFloorService {

    private final ParkingFloorRepository parkingFloorRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Override
    public ParkingFloorResponse addFloor(ParkingFloorRequest request) {
        ParkingLot parkingLot = parkingLotRepository.findById(request.getParkingLotId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found with id: " + request.getParkingLotId()));

        ParkingFloor floor = ParkingFloor.builder()
                .floorNumber(request.getFloorNumber())
                .parkingLot(parkingLot)
                .build();

        ParkingFloor saved = parkingFloorRepository.save(floor);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ParkingFloorResponse getFloorById(Long id) {
        ParkingFloor floor = parkingFloorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking floor not found with id: " + id));
        return mapToResponse(floor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkingFloorResponse> getFloorsByParkingLotId(Long parkingLotId) {
        return parkingFloorRepository.findByParkingLotId(parkingLotId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ParkingFloorResponse mapToResponse(ParkingFloor floor) {
        return ParkingFloorResponse.builder()
                .id(floor.getId())
                .floorNumber(floor.getFloorNumber())
                .parkingLotId(floor.getParkingLot().getId())
                .parkingLotName(floor.getParkingLot().getName())
                .totalSpots(floor.getSpots() != null ? floor.getSpots().size() : 0)
                .build();
    }
}
