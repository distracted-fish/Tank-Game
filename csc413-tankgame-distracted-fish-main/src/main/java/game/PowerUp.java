package main.java.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class PowerUp extends GameObject {

    protected boolean state=true;

    public PowerUp(float x, float y, BufferedImage img){
        super(img, x,y);
        this.hitbox=new Rectangle((int)x+1,(int)y+2,img.getWidth()-4,img.getHeight()-4);

    }

    public boolean isAlive(){
        return state;
    }

    @Override
    public void handleCollision(GameObject with) {

    }

    @Override
    public boolean isCollidable() {
        return false;
    }
}
