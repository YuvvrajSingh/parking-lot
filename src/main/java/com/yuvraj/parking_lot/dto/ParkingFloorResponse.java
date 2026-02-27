package com.yuvraj.parking_lot.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingFloorResponse {

    private Long id;
    private Integer floorNumber;
    private Long parkingLotId;
    private String parkingLotName;
    private int totalSpots;
}
