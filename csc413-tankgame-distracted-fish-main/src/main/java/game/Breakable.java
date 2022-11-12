package main.java.game;

import main.java.Resources;

import java.awt.image.BufferedImage;

public class Breakable  extends  Wall {
    public float getDurability() {
        return durability;
    }

    private float durability;

    public Breakable(int dur, float x, float y, BufferedImage img) {
        super(x, y, img);
        this.durability = dur;
    }

    public void takeDamage(float dmg) {
        this.durability -= dmg;

    }

    @Override
    public void handleCollision(GameObject with) {
        if (with instanceof Bullet) {
            this.takeDamage(((Bullet) with).getDmg());
            updateImage();
        }
    }

    private void updateImage() {
        if (durability == 2) {
            super.img = Resources.getImage("cracked");
        }
        if (durability == 1) {
            super.img = Resources.getImage("damaged");
        }
    }

//    void drawImage(Graphics2D buffer) {
//     //   buffer.setColor(Color.green);
//     //   buffer.drawRect((int) x + 2, (int) y + 2, this.img.getWidth() - 4, this.img.getHeight() - 4);
//    }
}
