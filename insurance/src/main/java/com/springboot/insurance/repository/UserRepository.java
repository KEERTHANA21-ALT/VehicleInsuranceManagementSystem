package com.springboot.insurance.repository;

import com.springboot.insurance.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    // Jpql query
    @Query("""
            select u from User u where u.username=?1 and isActivated=true
            """)
    Optional<User> loadUserByUsername(String username);


    Boolean existsByUsername(@NotBlank(message = "Username should not be empty") String username);
}
