package com.example.rfidtracking.service;

import com.example.rfidtracking.dto.FilialDTO;
import com.example.rfidtracking.model.Filial;
import com.example.rfidtracking.repository.FilialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilialService {

    @Autowired
    private FilialRepository filialRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<FilialDTO> listar(Pageable pageable, String nome, String cidade, String estado) {
        if (nome != null && !nome.isEmpty()) {
            return filialRepository.findByNomeContainingIgnoreCase(nome, pageable)
                    .map(this::convertToDto);
        } else if (cidade != null && !cidade.isEmpty()) {
            return filialRepository.findByCidadeContainingIgnoreCase(cidade, pageable)
                    .map(this::convertToDto);
        } else if (estado != null && !estado.isEmpty()) {
            return filialRepository.findByEstadoContainingIgnoreCase(estado, pageable)
                    .map(this::convertToDto);
        }
        return filialRepository.findAll(pageable).map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public FilialDTO buscarPorId(Long id) {
        Filial filial = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + id));
        return convertToDto(filial);
    }

    @Transactional
    public FilialDTO salvar(FilialDTO dto) {
        Filial filial = convertToEntity(dto);
        Filial filialSalva = filialRepository.save(filial);
        return convertToDto(filialSalva);
    }

    @Transactional
    public FilialDTO atualizar(Long id, FilialDTO dto) {
        Filial filialExistente = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + id));

        filialExistente.setNome(dto.getNome());
        filialExistente.setCidade(dto.getCidade());
        filialExistente.setEstado(dto.getEstado());

        Filial filialAtualizada = filialRepository.save(filialExistente);
        return convertToDto(filialAtualizada);
    }

    @Transactional
    public void deletar(Long id) {
        if (!filialRepository.existsById(id)) {
            throw new EntityNotFoundException("Filial não encontrada com ID: " + id);
        }
        filialRepository.deleteById(id);
    }

    private FilialDTO convertToDto(Filial filial) {
        return modelMapper.map(filial, FilialDTO.class);
    }

    private Filial convertToEntity(FilialDTO dto) {
        return modelMapper.map(dto, Filial.class);
    }
}
