package com.yuvraj.parking_lot.repository;

import com.yuvraj.parking_lot.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlateAndExitTimeIsNull(String licensePlate);

    boolean existsByLicensePlateAndExitTimeIsNull(String licensePlate);
}
