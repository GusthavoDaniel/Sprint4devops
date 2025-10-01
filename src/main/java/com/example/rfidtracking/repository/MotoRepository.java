package com.example.rfidtracking.repository;

import com.example.rfidtracking.model.Moto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
    Page<Moto> findByModeloContainingIgnoreCase(String modelo, Pageable pageable);
    Page<Moto> findByPlacaContainingIgnoreCase(String placa, Pageable pageable);
    Page<Moto> findByStatusContainingIgnoreCase(String status, Pageable pageable);
}

