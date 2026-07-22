package com.springboot.insurance.repository;

import com.springboot.insurance.enums.PlanType;
import com.springboot.insurance.model.InsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan,Long> {

    @Query("""
            select i
            from InsurancePlan i
            where id=?1 and isActive=true
            """)
    Optional<InsurancePlan> fetchById(long id);

    List<InsurancePlan> getByPlanType(PlanType planType);
}
