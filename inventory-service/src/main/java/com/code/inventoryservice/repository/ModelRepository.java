package com.code.inventoryservice.repository;

import com.code.inventoryservice.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelRepository extends JpaRepository<Model, UUID>
{
}
