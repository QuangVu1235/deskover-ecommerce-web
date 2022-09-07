package com.deskover.reponsitory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.deskover.entity.Administrator;

import java.util.List;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Administrator findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByUsername(Administrator admin);
    Page<Administrator> findByActived(Boolean isActive, Pageable pageable);
    List<Administrator> findByActived(Boolean isActive);

    // Number of accounts by role

}