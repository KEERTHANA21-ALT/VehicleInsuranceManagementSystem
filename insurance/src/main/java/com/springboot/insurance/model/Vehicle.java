package com.springboot.insurance.model;


import com.springboot.insurance.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(nullable = false)
    private String vehicleModel;

    private int year;

    private boolean isActive = true;

    // for image uploading
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "policy_holder_id", nullable = false )
    private PolicyHolder policyHolder;


}
