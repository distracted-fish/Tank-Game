package main.java.game;

import main.java.Resources;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

public class HealthPowerUp extends PowerUp {


    public HealthPowerUp(float x, float y,BufferedImage img) {
        super(x, y, img);
    }

    @Override
    public void handleCollision(GameObject with) {
        if(with instanceof Tank){
            if(((Tank)with).modifyHealth(1)){
            state = false;
                Clip sound = Resources.getSound("pickup");
                sound.setFramePosition(0);
                sound.loop(0);
                sound.start();}
        }
    }


}
