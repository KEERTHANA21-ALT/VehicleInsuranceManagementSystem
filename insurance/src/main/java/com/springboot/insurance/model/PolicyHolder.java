package com.springboot.insurance.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "policy_holder")
public class PolicyHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String name;

    private LocalDate dob;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    private boolean active = true;

    private boolean deletionRequested = false;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
