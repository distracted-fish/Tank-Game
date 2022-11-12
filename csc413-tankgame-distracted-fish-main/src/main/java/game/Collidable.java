package main.java.game;

public interface Collidable {
    //Rectangle   getHitBox();
    void        handleCollision(GameObject with);
    boolean     isCollidable();
}
