package com.example.rfidtracking.service;

import com.example.rfidtracking.dto.MotoDTO;
import com.example.rfidtracking.model.Filial;
import com.example.rfidtracking.model.Moto;
import com.example.rfidtracking.repository.FilialRepository;
import com.example.rfidtracking.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MotoService {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private FilialRepository filialRepository; // Added FilialRepository

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<MotoDTO> listar(Pageable pageable, String modelo, String placa, String status) {
        // Basic filtering example, can be expanded with Specifications for more complex queries
        if (modelo != null && !modelo.isEmpty()) {
            return motoRepository.findByModeloContainingIgnoreCase(modelo, pageable)
                    .map(this::convertToDto);
        } else if (placa != null && !placa.isEmpty()) {
            return motoRepository.findByPlacaContainingIgnoreCase(placa, pageable)
                    .map(this::convertToDto);
        } else if (status != null && !status.isEmpty()) {
            return motoRepository.findByStatusContainingIgnoreCase(status, pageable)
                    .map(this::convertToDto);
        }
        return motoRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public MotoDTO buscarPorId(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com ID: " + id));
        return convertToDto(moto);
    }

    @Transactional
    public MotoDTO salvar(MotoDTO dto) {
        Moto moto = convertToEntity(dto);
        if (dto.getFilialId() != null) {
            Filial filial = filialRepository.findById(dto.getFilialId())
                    .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + dto.getFilialId()));
            moto.setFilial(filial);
        }
        Moto motoSalva = motoRepository.save(moto);
        return convertToDto(motoSalva);
    }

    @Transactional
    public MotoDTO atualizar(Long id, MotoDTO dto) {
        Moto motoExistente = motoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada com ID: " + id));

        // Update fields from DTO
        motoExistente.setModelo(dto.getModelo());
        motoExistente.setPlaca(dto.getPlaca());
        motoExistente.setStatus(dto.getStatus());

        if (dto.getFilialId() != null) {
            Filial filial = filialRepository.findById(dto.getFilialId())
                    .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + dto.getFilialId()));
            motoExistente.setFilial(filial);
        } else {
            motoExistente.setFilial(null); // Or handle as per business logic if filialId is null
        }

        Moto motoAtualizada = motoRepository.save(motoExistente);
        return convertToDto(motoAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new EntityNotFoundException("Moto não encontrada com ID: " + id);
        }
        motoRepository.deleteById(id);
    }

    private MotoDTO convertToDto(Moto moto) {
        MotoDTO dto = modelMapper.map(moto, MotoDTO.class);
        if (moto.getFilial() != null) {
            dto.setFilialId(moto.getFilial().getIdFilial());
            dto.setNomeFilial(moto.getFilial().getNome()); // Populate filial name for DTO
        }
        return dto;
    }

    private Moto convertToEntity(MotoDTO dto) {
        return modelMapper.map(dto, Moto.class);
        // Note: Filial association is handled separately in save/update methods
    }
}

