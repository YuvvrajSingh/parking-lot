package com.yuvraj.parking_lot.repository;

import com.yuvraj.parking_lot.entity.ParkingSpot;
import com.yuvraj.parking_lot.entity.enums.ParkingSpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findByFloorId(Long floorId);

    List<ParkingSpot> findByTypeAndIsOccupiedFalse(ParkingSpotType type);

    Optional<ParkingSpot> findFirstByTypeAndIsOccupiedFalse(ParkingSpotType type);

    long countByTypeAndIsOccupiedFalse(ParkingSpotType type);

    @Query("SELECT s.type, COUNT(s) FROM ParkingSpot s WHERE s.isOccupied = false GROUP BY s.type")
    List<Object[]> countAvailableSpotsByType();

    List<ParkingSpot> findByIsOccupiedFalse();
}
