package com.yuvraj.parking_lot.dto;

import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpotResponse {

    private Long id;
    private String spotNumber;
    private ParkingSpotType type;
    private Boolean isOccupied;
    private Long floorId;
    private Integer floorNumber;
}
