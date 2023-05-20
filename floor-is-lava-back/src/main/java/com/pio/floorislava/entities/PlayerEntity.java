package com.pio.floorislava.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;


@Entity
@Table(name = "players")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Coordinates coordinates;

    public PlayerEntity() {
    }

    public PlayerEntity(String username) {
        this.username = username;
        this.coordinates = new Coordinates();
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
