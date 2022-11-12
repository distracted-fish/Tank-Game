package main.java.game;

import main.java.Resources;

import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

public class AttackSpeedPowerUp extends PowerUp{
    public AttackSpeedPowerUp(float x, float y, BufferedImage img) {
        super(x, y, img);
    }

    public void handleCollision(GameObject with) {
        if(with instanceof Tank){
            if(((Tank)with).modifyAttackSpeed(1.2F)) {
                state = false;
                Clip sound = Resources.getSound("pickup");
                sound.setFramePosition(0);
                sound.loop(0);
                sound.start();
            }
        }
    }
}
