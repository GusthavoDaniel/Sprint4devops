package com.example.rfidtracking.repository;

import com.example.rfidtracking.model.Filial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilialRepository extends JpaRepository<Filial, Long> {
    Page<Filial> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Filial> findByCidadeContainingIgnoreCase(String cidade, Pageable pageable);
    Page<Filial> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);
}

