package com.pio.floorislava.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coordinates {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    Integer posX;
    Integer posY;

    public Coordinates() {
        this.posX = 0;
        this.posY = 0;
    }

    public Coordinates(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }
}
