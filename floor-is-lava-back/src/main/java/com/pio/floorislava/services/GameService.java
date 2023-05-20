package com.pio.floorislava.services;

import com.pio.floorislava.entities.Game;
import com.pio.floorislava.entities.Player;
import com.pio.floorislava.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private PlayerRepository playerRepository;
    private final Game game = new Game();

    @Transactional
    public Optional<Player> savePlayer(Player player) {
        Optional<Player> possiblePlayer = playerRepository.findByUsername(player.getUsername());

        if (possiblePlayer.isPresent()) {
            return Optional.empty();
        } else {
            Player newPlayer = new Player(player.getUsername());
            return Optional.of(playerRepository.save(newPlayer));
        }
    }

    @Transactional
    public boolean removePlayer(Player player) {
        Optional<Player> possiblePlayer = playerRepository.findById(player.getId());

        if (possiblePlayer.isPresent()) {
            playerRepository.deleteById(possiblePlayer.get().getId());
            return true;
        }

        return false;
    }

    public Game buildGameInfo() {
        game.setPlayers(playerRepository.findAll());
        return game;
    }
}
