package main.java.game;

import main.java.Resources;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

public class SoymilkPowerUp extends PowerUp{
    public SoymilkPowerUp(float x, float y, BufferedImage img) {
        super(x, y, img);
    }
    public void handleCollision(GameObject with) {
        if(with instanceof Tank){
            if(((Tank)with).modifyAttackSpeed(2F)) {
                ((Tank)with).modifyDmg(.25f);
                state = false;
                Clip sound = Resources.getSound("pickup");
                sound.setFramePosition(0);
                sound.loop(0);
                sound.start();
            }

        }
    }
}
