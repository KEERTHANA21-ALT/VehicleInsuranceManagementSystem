package com.springboot.insurance.repository;

import com.springboot.insurance.dto.response.PolicyHolderResponseDto;
import com.springboot.insurance.model.PolicyHolder;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PolicyHolderRepository extends JpaRepository<PolicyHolder,Long> {

//    @Query("""
//            Select p
//            from PolicyHolder p
//            where isActive = true
//            """)
//    Page<PolicyHolder> fetchAll(Pageable pageable);

    @Query("""
            select p
            from  PolicyHolder p
            where id=?1 and active = true
            """)

    Optional<PolicyHolder> fetchById(long id);

    Optional<PolicyHolder> findByUserUsername(String userName);


    List<PolicyHolder> findByDeletionRequestedTrueOrActiveFalse();
}
