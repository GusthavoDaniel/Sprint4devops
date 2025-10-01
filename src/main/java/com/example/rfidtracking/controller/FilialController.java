package com.example.rfidtracking.controller;

import com.example.rfidtracking.dto.FilialDTO;
import com.example.rfidtracking.service.FilialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/filiais")
public class FilialController {

    @Autowired
    private FilialService filialService;

    @GetMapping
    public ResponseEntity<Page<FilialDTO>> listar(
            Pageable pageable,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado) {
        Page<FilialDTO> pagina = filialService.listar(pageable, nome, cidade, estado);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilialDTO> buscarPorId(@PathVariable Long id) {
        FilialDTO dto = filialService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<FilialDTO> criar(@RequestBody @Valid FilialDTO dto) {
        FilialDTO filialSalva = filialService.salvar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(filialSalva.getIdFilial()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(filialSalva, headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilialDTO> atualizar(@PathVariable Long id, @RequestBody @Valid FilialDTO dto) {
        FilialDTO filialAtualizada = filialService.atualizar(id, dto);
        return ResponseEntity.ok(filialAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        filialService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
