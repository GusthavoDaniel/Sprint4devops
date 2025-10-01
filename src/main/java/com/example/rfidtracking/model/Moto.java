package com.example.rfidtracking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "MOTO") // Explicitly naming table as per Oracle XML
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming sequence/identity for Oracle, adjust if needed
    @Column(name = "id_moto")
    private Long id;

    @NotBlank
    @Column(name = "modelo")
    private String modelo;

    @NotBlank
    @Column(name = "placa")
    private String placa;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching is generally a good default
    @JoinColumn(name = "filial_id")
    private Filial filial;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroRFID> registros;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public List<RegistroRFID> getRegistros() {
        return registros;
    }

    public void setRegistros(List<RegistroRFID> registros) {
        this.registros = registros;
    }
}

