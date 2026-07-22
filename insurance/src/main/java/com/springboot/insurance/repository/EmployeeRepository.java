package com.springboot.insurance.repository;

import com.springboot.insurance.enums.EmployeeRole;
import com.springboot.insurance.enums.Role;
import com.springboot.insurance.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("""
            select e
            from Employee e 
            where id=?1 and isActive=true
            """)
    Optional<Employee> fetchById(long id);

    List<Employee> getByEmployeeRole(EmployeeRole employeeRole);
}
