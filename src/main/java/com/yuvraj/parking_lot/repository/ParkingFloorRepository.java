package com.yuvraj.parking_lot.repository;

import com.yuvraj.parking_lot.entity.ParkingFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingFloorRepository extends JpaRepository<ParkingFloor, Long> {

    List<ParkingFloor> findByParkingLotId(Long parkingLotId);
}
