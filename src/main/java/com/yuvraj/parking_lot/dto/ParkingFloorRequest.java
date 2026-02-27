package com.yuvraj.parking_lot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingFloorRequest {

    @NotNull(message = "Floor number is required")
    private Integer floorNumber;

    @NotNull(message = "Parking lot ID is required")
    private Long parkingLotId;
}
