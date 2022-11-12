package main.java.game;

import java.util.List;

public class Collision {
    Collision() {
    }

    public void checkCollisions(List<GameObject> objs, Tank t, Tank o) {
        for (int i = 0; i < objs.size(); i++) {
            GameObject ob = objs.get(i);

            //check players bullets


            if (o.getHitbox().intersects(ob.getHitbox())) {
                o.handleCollision(ob);
            }
            if (t.getHitbox().intersects(ob.getHitbox())) {
                t.handleCollision(ob);
            }
            if (t.getHitbox().intersects(o.getHitbox())) {
                t.handleCollision(o);
                o.handleCollision(t);
            }
            for (int j = 0; j < t.bullets.size(); j++) {
                Bullet b = (Bullet) t.bullets.get(j);
                if (b.getHitbox().intersects(ob.getHitbox())) {
                    b.handleCollision(ob);
                }
                if (b.getHitbox().intersects(o.getHitbox())) {
                    o.handleCollision(b);
                    t.removeBullet(b);
                }
            }

            //check other players bullets
            for (int j = 0; j < o.bullets.size(); j++) {
                Bullet b = (Bullet) o.bullets.get(j);
                if (b.getHitbox().intersects(ob.getHitbox())) {
                    b.handleCollision(ob);
                }
                if (b.getHitbox().intersects(t.getHitbox())) {
                    t.handleCollision(b);
                    o.removeBullet(b);
                }
            }

            if (ob instanceof PowerUp) {
                if (!((PowerUp) ob).isAlive()) {
                    objs.remove(ob);
                }
            }
        }
    }
}
