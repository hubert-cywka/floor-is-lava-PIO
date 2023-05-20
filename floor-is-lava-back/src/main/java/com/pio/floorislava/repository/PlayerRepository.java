package com.pio.floorislava.repository;

import com.pio.floorislava.entities.PlayerEntity;
import com.pio.floorislava.entities.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findByUsername(String username);
    Optional<PlayerEntity> findByCoordinates(Coordinates coordinates);
    List<PlayerEntity> findAllByCoordinates(Coordinates coordinates);
}
