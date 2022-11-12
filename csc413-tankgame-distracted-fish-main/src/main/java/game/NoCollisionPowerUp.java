package main.java.game;

import java.awt.image.BufferedImage;

public class NoCollisionPowerUp extends PowerUp {
    public NoCollisionPowerUp(float x, float y, BufferedImage img) {
        super(x, y, img);
    }
    public void handleCollision(GameObject with) {

    }
}
