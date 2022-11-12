/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.game;


import main.java.GameConstants;
import main.java.Launcher;
import main.java.Resources;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;


/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;

    private Launcher lf;
    private long tick = 0;
   // List<Animation> anime= new ArrayList<>();
    List<GameObject> objs = new ArrayList<>();
    List<Tank> players = new ArrayList<>();
    Collision coll = new Collision();
    Clip theme;


    /**
     * 
     * @param lf
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {

            this.resetGame();
            theme= Resources.getSound("theme1");
            theme.setFramePosition(0);
            theme.loop(0);
            theme.start();
            while (true) {
                this.tick++;

                for (Tank p: players) {
                    p.update();
                }

//
                coll.checkCollisions(objs,players.get(0),players.get(1));
                this.repaint();   // redraw game

                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */
                Thread.sleep(1000 / 144);

                if(players.get(0).isDead()||players.get(1).isDead()){
                    if(players.get(0).getLives()>0&&players.get(1).getLives()>0) {
                        Clip sound = Resources.getSound("death");
                        sound.setFramePosition(0);
                        sound.loop(0);
                        sound.start();
                        resetGame();
                    }
                    else {
                        this.lf.setFrame("end");
                        return;
                    }
                }

            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.players.get(0).setX(300);
        this.players.get(0).setY(300);
        this.players.get(1).setX(960);
        this.players.get(1).setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        int playerCount = 2;
        Resources.initImages();
        Resources.initAudio();
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                GameConstants.WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        initPlayers(playerCount);
        initPlayerControls(playerCount);
        initMap();
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0,0,GameConstants.WORLD_WIDTH,GameConstants.WORLD_HEIGHT);
        buffer.drawImage(Resources.getImage("floor"),0,0,null);

        checkBreakable(buffer);

        for (int i = 0; i < players.size(); i++) {
            Tank p = players.get(i);
            p.drawImage(buffer);
        }
        g2.drawImage(splitScreen(this.players.get(0)),0,0,null);
        g2.drawImage(splitScreen(this.players.get(1)),GameConstants.GAME_SCREEN_WIDTH/2+4,0,null);
        drawPlayerLives(g2,this.players.get(0),30);
        drawPlayerLives(g2,this.players.get(1),GameConstants.GAME_SCREEN_WIDTH/2+34);
        drawMinimap(g2);
    }

    private BufferedImage splitScreen(Tank t){
        return world.getSubimage((int)getSplitScreenX(t), (int)getSplitScreenY(t),GameConstants.GAME_SCREEN_WIDTH/2-2, GameConstants.GAME_SCREEN_HEIGHT);

    }
    private float getSplitScreenX(Tank t){
        int xOffset=GameConstants.GAME_SCREEN_WIDTH/4;

        float xSplitStart=t.getX()-xOffset;

        if(xSplitStart<0){
            xSplitStart=0;
        }
        else if(t.getX()+xOffset>GameConstants.WORLD_WIDTH){
            xSplitStart=GameConstants.WORLD_WIDTH-(2*xOffset)+2;
        }
        return xSplitStart;
    }
    private float getSplitScreenY(Tank t){
        int yOffset=GameConstants.GAME_SCREEN_HEIGHT/2;

        float ySplitStart=t.getY()-yOffset;

        if(ySplitStart<=0){
            ySplitStart=0;
        }
        else if(t.getY()+yOffset>GameConstants.WORLD_HEIGHT){
            ySplitStart=GameConstants.WORLD_HEIGHT-GameConstants.GAME_SCREEN_HEIGHT;
        }
        return ySplitStart;
    }

    private void drawMinimap(Graphics2D g2){
        BufferedImage mm = world.getSubimage(0,0,GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        g2.scale(.15,.15);
        g2.drawImage(mm,2840,3044,null);
    }
    private void drawPlayerLives(Graphics2D g,Tank t,int x){
        for(int i=0; i<t.getLives();i++){
            AffineTransform rotation = AffineTransform.getTranslateInstance(x+(i*30), 30);
            rotation.rotate(Math.toRadians(-45), t.img.getWidth() / 2.0, t.img.getHeight() / 2.0);
            g.drawImage(t.img,rotation ,null);
        }
    }
    private void initMap(){
        try(BufferedReader mapReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(GameWorld.class.getClassLoader().getResourceAsStream("maps/tank_map.csv"))))){
            String[] size = mapReader.readLine().split(",");
            int numOfRows = Integer.parseInt(size[0]);
            int numOfColumns = Integer.parseInt(size[1]);
            for (int i = 0; mapReader.ready();i++){
                String[] items = mapReader.readLine().split("");
                for (int j = 0; j < items.length; j++) {
                    switch (items[j]){
                        case "8" -> {
                            Wall w = new Wall(j*30,i*30,Resources.getImage("unbreak"));
                            objs.add(w);
                        }
                        case "-"-> {
                            Wall w = new Wall(j*30,i*30,Resources.getImage("horizontal"));
                            objs.add(w);
                        }
                        case "|"-> {
                            Wall w = new Wall(j*30,i*30,Resources.getImage("vertical"));
                            objs.add(w);
                        }
                        case "Q"-> {
                            Wall w = new Wall(j*30,i*30,Resources.getImage("top-cap-left"));
                            objs.add(w);
                        }
                        case "P"-> {
                            Wall w = new Wall(j*30,i*30,Resources.getImage("top-cap-right"));
                            objs.add(w);
                        }
                        case "Z"-> {
                            Wall w = new Wall(j*30,i*30,Resources.getImage("bottom-cap-left"));
                            objs.add(w);
                        }
                        case "M"-> {
                            Wall w = new Wall(j*30,i*30,Resources.getImage("bottom-cap-right"));
                            objs.add(w);
                        }
                        case "9"-> {
                            Breakable b = new Breakable(3,j*30,i*30,Resources.getImage("breakable"));
                            int rand_int = ThreadLocalRandom.current().nextInt();
                            if(rand_int%5 <= 3){
                                int rand_int2 = ThreadLocalRandom.current().nextInt();;
                                switch (rand_int2%5){
                                    case 0 -> {
                                        PowerUp pb = new ArmorPowerUp(j*30+2,i*30+2,Resources.getImage("blue"));
                                        objs.add(pb);
                                    }
                                    case 1 -> {
                                        PowerUp pr = new HealthPowerUp(j*30+2,i*30+2,Resources.getImage("red"));
                                        objs.add(pr);
                                    }
                                    case 2 -> {
                                        PowerUp pg = new SpeedPowerUp(j*30+2,i*30+2,Resources.getImage("green"));
                                        objs.add(pg);
                                    }
                                    case 3 -> {
                                        PowerUp as = new AttackSpeedPowerUp(j*30+2,i*30+2,Resources.getImage("atSpd"));
                                        objs.add(as);
                                    }
                                    case 4 -> {
                                        PowerUp as = new SoymilkPowerUp(j*30+2,i*30+2,Resources.getImage("soy"));
                                        objs.add(as);
                                    }
                                }
                            }
                            objs.add(b);
                        }


                    }
                }

            }
        }catch(IOException e){
            System.out.println(e);
            System.exit(-2);
        }
    }

    private void initPlayers(int playerCount){
        int playerX=0, playerY=0, playerAngle = 0;
        String key = "tank";
        for (int i = 1; i <= playerCount; i++) {
            playerX += 300;
            Tank player = new Tank(playerX, playerY,0,0,(short) playerAngle,Resources.getImage(key+i));
            players.add(player);
            playerAngle +=180;
        }
    }

    private void initPlayerControls(int playerCount){
        for (int i = playerCount; i > 0; i--) {
            switch (i) {
                case 1 -> {
                    TankControl tc1 = new TankControl(players.get(0), KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
                    this.lf.getJf().addKeyListener(tc1);
                }
                case 2 -> {
                    TankControl tc2 = new TankControl(players.get(1), KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT);
                    this.lf.getJf().addKeyListener(tc2);
                }
            }
        }
    }

    private void checkBreakable(Graphics2D g){
        for (int i = 0; i < objs.size(); i++) {
            GameObject o = objs.get(i);
            if(o instanceof Breakable){
                if(((Breakable) o).getDurability()<=0){
                    objs.remove(o);
                }
            }
            o.drawImage(g);
        }
    }


}


