package com.kodlamaio.rentalservice.repository;

import com.kodlamaio.rentalservice.entities.Rental;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.swing.plaf.nimbus.State;
import java.util.UUID;

public interface RentalRepository extends JpaRepository<Rental, UUID>
{

}
