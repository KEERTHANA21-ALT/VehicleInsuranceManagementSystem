package com.springboot.insurance.repository;

import com.springboot.insurance.model.Addon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.net.ContentHandler;

public interface AddonRepository extends JpaRepository<Addon,Long> {

}
