package com.code.inventoryservice.repository;

import com.code.inventoryservice.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID>
{
}
