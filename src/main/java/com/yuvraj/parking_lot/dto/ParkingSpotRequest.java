package com.yuvraj.parking_lot.dto;

import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpotRequest {

    @NotBlank(message = "Spot number is required")
    private String spotNumber;

    @NotNull(message = "Spot type is required")
    private ParkingSpotType type;

    @NotNull(message = "Floor ID is required")
    private Long floorId;
}
