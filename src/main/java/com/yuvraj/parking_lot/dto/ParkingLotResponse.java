package com.yuvraj.parking_lot.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLotResponse {

    private Long id;
    private String name;
    private String address;
    private int totalFloors;
}
