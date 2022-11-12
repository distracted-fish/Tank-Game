package main.java.game;

import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    public Wall(float x, float y, BufferedImage img){
        super(img, x,y);

    }


    @Override
    public void handleCollision(GameObject with) {

    }

    @Override
    public boolean isCollidable() {
        return true;
    }
}
