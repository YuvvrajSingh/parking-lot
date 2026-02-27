package com.yuvraj.parking_lot.service.impl;

import com.yuvraj.parking_lot.dto.AvailabilityResponse;
import com.yuvraj.parking_lot.dto.ParkRequest;
import com.yuvraj.parking_lot.dto.VehicleResponse;
import com.yuvraj.parking_lot.entity.ParkingSpot;
import com.yuvraj.parking_lot.entity.Vehicle;
import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import com.yuvraj.parking_lot.exception.SpotNotAvailableException;
import com.yuvraj.parking_lot.exception.VehicleAlreadyParkedException;
import com.yuvraj.parking_lot.exception.VehicleNotFoundException;
import com.yuvraj.parking_lot.repository.ParkingSpotRepository;
import com.yuvraj.parking_lot.repository.VehicleRepository;
import com.yuvraj.parking_lot.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingServiceImpl implements ParkingService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    @Override
    public VehicleResponse parkVehicle(ParkRequest request) {
        // Check if vehicle is already parked
        if (vehicleRepository.existsByLicensePlateAndExitTimeIsNull(request.getLicensePlate())) {
            throw new VehicleAlreadyParkedException(
                    "Vehicle with license plate " + request.getLicensePlate() + " is already parked");
        }

        // Find an available spot of the requested type
        ParkingSpot spot = parkingSpotRepository.findFirstByTypeAndIsOccupiedFalse(request.getSpotType())
                .orElseThrow(() -> new SpotNotAvailableException(
                        "No available " + request.getSpotType() + " spot found"));

        // Mark spot as occupied
        spot.setIsOccupied(true);
        parkingSpotRepository.save(spot);

        // Create vehicle record
        Vehicle vehicle = Vehicle.builder()
                .licensePlate(request.getLicensePlate())
                .vehicleType(request.getVehicleType())
                .entryTime(LocalDateTime.now())
                .parkingSpot(spot)
                .build();

        Vehicle saved = vehicleRepository.save(vehicle);
        return mapToResponse(saved);
    }

    @Override
    public VehicleResponse exitVehicle(String licensePlate) {
        // Find the currently parked vehicle
        Vehicle vehicle = vehicleRepository.findByLicensePlateAndExitTimeIsNull(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "No parked vehicle found with license plate: " + licensePlate));

        // Free the spot
        ParkingSpot spot = vehicle.getParkingSpot();
        if (spot != null) {
            spot.setIsOccupied(false);
            parkingSpotRepository.save(spot);
        }

        // Record exit
        vehicle.setExitTime(LocalDateTime.now());
        vehicle.setParkingSpot(null);
        Vehicle saved = vehicleRepository.save(vehicle);

        VehicleResponse response = mapToResponse(saved);
        // Include spot info for reference even after exit
        if (spot != null) {
            response.setSpotNumber(spot.getSpotNumber());
            response.setSpotType(spot.getType().name());
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponse getAvailableSpots() {
        List<Object[]> results = parkingSpotRepository.countAvailableSpotsByType();

        Map<ParkingSpotType, Long> availabilityMap = new EnumMap<>(ParkingSpotType.class);
        long totalAvailable = 0;

        for (Object[] row : results) {
            ParkingSpotType type = (ParkingSpotType) row[0];
            Long count = (Long) row[1];
            availabilityMap.put(type, count);
            totalAvailable += count;
        }

        // Include types with 0 availability
        for (ParkingSpotType type : ParkingSpotType.values()) {
            availabilityMap.putIfAbsent(type, 0L);
        }

        return AvailabilityResponse.builder()
                .availableSpots(availabilityMap)
                .totalAvailable(totalAvailable)
                .build();
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        VehicleResponse.VehicleResponseBuilder builder = VehicleResponse.builder()
                .id(vehicle.getId())
                .licensePlate(vehicle.getLicensePlate())
                .vehicleType(vehicle.getVehicleType())
                .entryTime(vehicle.getEntryTime())
                .exitTime(vehicle.getExitTime());

        if (vehicle.getParkingSpot() != null) {
            builder.spotNumber(vehicle.getParkingSpot().getSpotNumber());
            builder.spotType(vehicle.getParkingSpot().getType().name());
        }

        return builder.build();
    }
}
