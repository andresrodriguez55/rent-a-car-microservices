package com.code.inventoryservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity //imzalama işlemi -> kısıt kullanılmış oluyor
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
public class Brand
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    //relationships
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Model> models;
}

// car
// model, her markanın modelleri vardır

