package com.springboot.insurance.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Addon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //ZERO_DEPRECIATION,ROADSIDE_ASSISTANCE,ENGINE_PROTECTION

    private double price;

    private String description;

    private boolean isActive = true;


}
