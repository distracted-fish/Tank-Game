package main.java.game;

import main.java.GameConstants;
import main.java.Resources;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */

public class Tank extends GameObject implements Collidable{

    private int maxHealth=10;
    private int lives = 3;
    private int maxArmor = 5;
    private float maxSpeed=5F;
    private float dmg =1;


    private float vx;

    private float vy;
    private float angle;
    private int health;

    private int armor;
    private float R = 1F;

    private float ROTATIONSPEED = 1f;
    private boolean UpPressed;

    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;
    List<GameObject> bullets = new ArrayList<>();
    List<PowerUp> mods = new ArrayList<>();

    //BulletPool backpack ;
    float fireDelay = 120f;
    float coolDown = 0f;
    float rateOfFire = 1.5f;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(img,x,y);
        this.hitbox=new Rectangle((int)x+2,(int)y+2,img.getWidth()-5,img.getHeight()-5);
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.health = 10;
        this.armor = 0;
    }

    // Tank Position setter and getters
    void setX(float x){
        this.x = x;
        this.hitbox.setLocation((int)x,(int)y);
    }

    void setY(float y) { this. y = y;
        this.hitbox.setLocation((int)x,(int)y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle(){return angle;}


    //button pressed toggles
    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleShootPressed() { this.ShootPressed = true; }

    void unToggleShootPressed() { this.ShootPressed = false; }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }



    void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.ShootPressed&& this.coolDown >= this.fireDelay) {
            this.coolDown =0;
           Clip shot = Resources.getSound("bubble_shot");
            shot.setFramePosition(0);  // Must always rewind!
            shot.loop(0);
            shot.start();
            Bullet b = new Bullet(this.setBulletStartX(), this.setBulletStartY(), angle, Resources.getImage("bullet"),this.dmg);
            bullets.add(b);
        }
        this.coolDown += this.rateOfFire;
        for (int i = 0; i <bullets.size() ; i++) {
            Bullet b = (Bullet) bullets.get(i);
            if (!b.isAlive()){
                removeBullet(b);
            }
        }
    }
    public void removeBullet(Bullet b){
        Clip pop = Resources.getSound("bubble_pop");
        pop.setFramePosition(0);
        pop.loop(0);
        pop.start();
         bullets.remove(b);
    }
    protected int setBulletStartX(){
        float cx = 35f*(float)Math.cos(Math.toRadians(angle));
        return (int) x+this.img.getWidth()/2+(int) cx-9;
    }
    int setBulletStartY(){
        float cy = 35f*(float)Math.sin(Math.toRadians(angle));
        return (int) y+this.img.getHeight()/2+(int) cy-7;
    }

    //Tank momvement
    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
        this.hitbox.setLocation((int)x,(int)y);
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int)x,(int)y);
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.WORLD_WIDTH - 90) {
            x = GameConstants.WORLD_WIDTH - 90;
        }
        if (y < 30) {
            y = 30;
        }
        if (y >= GameConstants.WORLD_HEIGHT - 90) {
            y = GameConstants.WORLD_HEIGHT - 90;
        }
    }


    //Collision
    @Override
    public void handleCollision(GameObject with) {
        if(with instanceof Bullet){
            with.handleCollision(this);
        }
        if(with instanceof PowerUp){
            with.handleCollision(this);
        }
        if(with instanceof Tank){
            if(this.UpPressed) {
                x -= vx*.007;
                y -= vy*.007;
            }
            if(this.DownPressed){
                x += vx*.007;
                y += vy*.007;
            }
        }
        if(with instanceof Wall){
            if(this.UpPressed) {
                x -= vx;
                y -= vy;
            }
            if(this.DownPressed){
                x += vx;
                y += vy;
            }
        }
    }

    @Override
    public boolean isCollidable() {
        return true;
    }


    //Draw stuff
    @Override
    public void drawImage(Graphics2D g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        g.drawImage(this.img, rotation, null);
        g.setColor(Color.RED);

        for (int i = 0; i < this.bullets.size(); i++) {
            Bullet b = (Bullet) bullets.get(i);
            b.drawImage(g);
        }
      //  drawHitbox(g);
        drawHealth(g);
        drawArmor(g);
    }
    public void drawHitbox(Graphics2D g2d){
        g2d.setColor(Color.MAGENTA);
        g2d.drawRect((int) x, (int) y, this.img.getWidth(),this.img.getHeight());
    }
    public void takeDamage(float dmg){
        if(armor<1){
            this.health -= dmg;
        } else armor -= dmg;
    }
    public void drawHealth(Graphics2D g2d){

        BufferedImage full = Resources.getImage("heart");
        BufferedImage half = Resources.getImage("half-heart");
        BufferedImage empty = Resources.getImage("empty-heart");
        int j=0;
        int l=28;
        for(int i=0;i<maxHealth;){
            for (int k = 0; k <health/2 ; k++) {
                g2d.drawImage(full,(int)x+j,(int) y-l,null);
                i+=2;
                j+=20;
            }
            if(health%2==1){
                g2d.drawImage(half,(int)x+j,(int) y-l,null);
                j+=20;
                i+=2;
            }
            for (int k = 0; k < (maxHealth-health)/2; k++) {
                g2d.drawImage(empty,(int)x+j,(int) y-l,null);
                j+=20;
                i+=2;
            }
        }
    }
    public void drawArmor(Graphics2D g){
        BufferedImage shell = Resources.getImage("shell");
        for (int j=0,i = 0; i < armor; i++) {

            g.drawImage(shell, (int)x+j,(int)y-15,null);
            j+=21;
        }
    }


    // Stat modifiers
    public int getLives() {
        return lives;
    }
    public void loseLife(){
        if(lives!=0){
            lives--;
        }
    }

    public boolean isDead(){
        if((health<=0)){
            lives--;
            health=maxHealth;
            return true;
        }else return false;
    }
    public boolean modifyHealth(int h) {
        if(this.health != maxHealth){
            this.health +=h;
            return true;
        }
        else return false;
    }
    public boolean modifyArmor(int i) {
        if(this.armor != maxArmor){
            this.armor +=i;
            return true;
        }
        else return false;

    }
    public boolean modifySpeed(float i) {
        if(R!=maxSpeed){
            this.R+=i;
            return true;
        }
        else return false;
    }

    public boolean modifyAttackSpeed(float v) {
        this.rateOfFire *= v;
        return true;
        //no max speed
    }
    public void modifyDmg(float v) {
        this.dmg *= v;
        //no max dmg
    }
}
