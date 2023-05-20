package com.pio.floorislava.controllers;

import com.pio.floorislava.entities.Game;
import com.pio.floorislava.entities.Player;
import com.pio.floorislava.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/connect")
    public ResponseEntity<Game> connectToGame(@RequestBody Player player) {
        Optional<Player> newPlayer = gameService.savePlayer(player);
        if (newPlayer.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(gameService.buildGameInfo());
    }

    @GetMapping("/info")
    @SendTo("/game/progress")
    public Game getGameInfo() {
        return gameService.buildGameInfo();
    }
}
