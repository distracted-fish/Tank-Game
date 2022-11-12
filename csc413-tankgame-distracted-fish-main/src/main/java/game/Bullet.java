package main.java.game;

import main.java.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject implements Collidable {

    private float angle;
    private float dmg;
    boolean alive = true;
    private float R = 3;
    private boolean collidable = true;

    public Bullet(float x, float y, float angle,BufferedImage img, float dmg) {
        super(img,x,y);
        this.angle = angle;
        this.dmg = dmg;
    }

    public void update() {
        x += Math.round(R * Math.cos(Math.toRadians(angle)));
        y += Math.round(R * Math.sin(Math.toRadians(angle)));
        this.hitbox.setLocation((int)x+2,(int)y+2);
    }
    public boolean isAlive(){
        if(!alive){
            return false;
        }
        else if(x > 30 && x < GameConstants.WORLD_WIDTH - 88 && y>40 && y <GameConstants.WORLD_HEIGHT - 80){
            update();
            return alive;
        }
        return false;
    }

    public void drawImage(Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g.drawImage(this.img, rotation, null);
        g.setColor(Color.RED);

    }

    public float getDmg(){
        return this.dmg;
    }

    @Override
    public void handleCollision(GameObject with) {
        if(with instanceof Wall){
            if(with instanceof Breakable){
                with.handleCollision(this);
            }
            this.alive = !collidable;
        }
        if(with instanceof Tank){
            ((Tank) with).takeDamage(this.getDmg());
            alive = false;
        }
    }

    @Override
    public boolean isCollidable() {
        return collidable;
    }
    public void setCollidable(boolean b){
        collidable = b;
    }
}
