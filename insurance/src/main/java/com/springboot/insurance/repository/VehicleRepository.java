package com.springboot.insurance.repository;

import com.springboot.insurance.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {

    Optional<Vehicle> getByVehicleNumber(String vehicleNumber);

    // in repo wrapper cls works
    // naming the method in repo is imp
    List<Vehicle> findByPolicyHolderId(Long policyHolderId);
}
