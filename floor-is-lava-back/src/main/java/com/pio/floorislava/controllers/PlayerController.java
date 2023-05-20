package com.pio.floorislava.controllers;

import com.pio.floorislava.entities.PlayerEntity;
import com.pio.floorislava.repository.PlayerRepository;
import com.pio.floorislava.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<List<PlayerEntity>> getAllPlayers() {
        try {
            List<PlayerEntity> players = playerRepository.findAll();

            if (players.isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(players, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/player")
    public ResponseEntity<PlayerEntity> addPlayer(@RequestBody PlayerEntity player) {
        try {
            Optional<PlayerEntity> presentPlayer = playerRepository.findByUsername(player.getUsername());

            if (presentPlayer.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            PlayerEntity newPlayer = playerRepository.save(player);
            return new ResponseEntity<>(newPlayer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
