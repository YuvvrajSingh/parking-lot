package com.yuvraj.parking_lot.service.impl;

import com.yuvraj.parking_lot.dto.ParkingLotRequest;
import com.yuvraj.parking_lot.dto.ParkingLotResponse;
import com.yuvraj.parking_lot.entity.ParkingLot;
import com.yuvraj.parking_lot.exception.ResourceNotFoundException;
import com.yuvraj.parking_lot.repository.ParkingLotRepository;
import com.yuvraj.parking_lot.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    @Override
    public ParkingLotResponse createParkingLot(ParkingLotRequest request) {
        ParkingLot parkingLot = ParkingLot.builder()
                .name(request.getName())
                .address(request.getAddress())
                .build();

        ParkingLot saved = parkingLotRepository.save(parkingLot);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ParkingLotResponse getParkingLotById(Long id) {
        ParkingLot parkingLot = parkingLotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found with id: " + id));
        return mapToResponse(parkingLot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParkingLotResponse> getAllParkingLots() {
        return parkingLotRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ParkingLotResponse mapToResponse(ParkingLot parkingLot) {
        return ParkingLotResponse.builder()
                .id(parkingLot.getId())
                .name(parkingLot.getName())
                .address(parkingLot.getAddress())
                .totalFloors(parkingLot.getFloors() != null ? parkingLot.getFloors().size() : 0)
                .build();
    }
}
