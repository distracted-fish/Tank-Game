package main.java;

import main.java.game.GameWorld;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Resources {
    private static Map<String, BufferedImage> images = new HashMap<>();
    private static Map<String, Clip> sounds = new HashMap<>();
    private static Map<String, List<BufferedImage>> animations = new HashMap();

    public static  BufferedImage getImage(String key) {
        return Resources.images.get(key);
    }

    public static  Clip getSound(String key) {
        return Resources.sounds.get(key);
    }

    public static  List<BufferedImage> getAnimation(String key) {
        return Resources.animations.get(key);
    }
    
    public static void initImages(){
        try {
            Resources.images.put("tank1", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank/Mantaray.png"))));
            Resources.images.put("tank2", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank/SeaTurtle.png"))));
            Resources.images.put("floor", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("floor/WaterFloor.png"))));
            Resources.images.put("unbreak", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/S171I0.png"))));
            Resources.images.put("breakable", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/S179I0.png"))));
            Resources.images.put("damaged", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/S1075I0.png"))));
            Resources.images.put("cracked", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/S1076I0.png"))));

            Resources.images.put("heart", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("status/Health1.png"))));
            Resources.images.put("half-heart", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("status/Health1Half.png"))));
            Resources.images.put("empty-heart", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("status/HealthEmpty.png"))));
            Resources.images.put("shell", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("status/ShellArmor.png"))));

            Resources.images.put("bullet", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("bullet/bubble.png"))));
            Resources.images.put("blue", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("powerup/S298I0.png"))));
            Resources.images.put("red", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("powerup/S298I1.png"))));
            Resources.images.put("green", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("powerup/S298I2.png"))));
            Resources.images.put("atSpd", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("powerup/atSpd.png"))));
            Resources.images.put("soy", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("powerup/S127I0.png"))));


            Resources.images.put("top-cap-left", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/Tile1.png"))));
            Resources.images.put("top-cap-right", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/Tile3.png"))));
            Resources.images.put("horizontal", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/Tile-.png"))));
            Resources.images.put("vertical", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/Tile_.png"))));
            Resources.images.put("bottom-cap-left", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/Tile7.png"))));
            Resources.images.put("bottom-cap-right", ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("walls/Tile9.png"))));




        } catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public static void initAudio(){
        try {
            AudioInputStream audio;
            Clip clip;
            audio = AudioSystem.getAudioInputStream(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("sounds/15-Zora.wav")));
            clip = AudioSystem.getClip();
            clip.open(audio);
            Resources.sounds.put("theme1",clip);

            audio = AudioSystem.getAudioInputStream(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("sounds/bubble_pop.wav")));
            clip = AudioSystem.getClip();
            clip.open(audio);
            Resources.sounds.put("bubble_pop",clip);

            audio = AudioSystem.getAudioInputStream(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("sounds/bubble_shot.wav")));
            clip = AudioSystem.getClip();
            clip.open(audio);
            Resources.sounds.put("bubble_shot",clip);

            audio = AudioSystem.getAudioInputStream(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("sounds/Bloom.wav")));
            clip = AudioSystem.getClip();
            clip.open(audio);
            Resources.sounds.put("pickup",clip);

            audio = AudioSystem.getAudioInputStream(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("sounds/Death.wav")));
            clip = AudioSystem.getClip();
            clip.open(audio);
            Resources.sounds.put("death",clip);

        }catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initAnimations(){
        //7.6 @01:05:32
        String baseName = "expl_08_%o4d.png";
        List<BufferedImage> temp = new ArrayList<>();
        try{
            for (int i = 0; i< 32; i++){
                String fName = String.format(baseName, i);
                String fullPath = "animations/bullet/"+ fName;
                temp.add(ImageIO.read(Objects.requireNonNull(GameWorld.class.getClassLoader().getResource(fullPath))));
            }

        } catch (IOException e){
            System.out.println(e);
            System.exit(-2);
        }
        Resources.animations.put("bullet", temp);
        
    }
    public static void main(String[] args){
        Resources.initImages();
       // Resources.initAnimations();
        Resources.initAudio();
    }
}
