package com.yuvraj.parking_lot.dto;

import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityResponse {

    private Map<ParkingSpotType, Long> availableSpots;
    private long totalAvailable;
}
