package main.java.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject implements Collidable{
    protected BufferedImage img;
    protected Rectangle hitbox;
    protected float x,y;

    public GameObject(BufferedImage img, float x, float y){
        this.img = img;
        this.x=x;
        this.y=y;
        this.hitbox=new Rectangle((int)x,(int)y,img.getWidth(),img.getHeight());
    }


    public Rectangle getHitbox(){
        return this.hitbox.getBounds();
    }

    void drawImage(Graphics2D buffer){
        buffer.drawImage(img, (int)x,(int)y , null);
        buffer.setColor(Color.green);
     //   buffer.drawRect((int) x+2, (int) y+2, this.img.getWidth()-4,this.img.getHeight()-4);
    }


    public float getX() {
        return x;
    }
    public float getY(){
        return y;
    }


}

