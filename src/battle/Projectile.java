package battle;

import java.awt.Rectangle;

public class Projectile {
    public double x;
    public double y;
    public double velX;
    public double velY;

    public int width = 8;
    public int height = 8;

    public int damage;
    public boolean isDead;

    public Projectile(double x, double y, double velX, double velY, int damage, boolean isDead) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.damage = damage;
        this.isDead = isDead;
    }

    public void update() {
        x += velX;
        y += velY;
    }

    public Rectangle getHitbox() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}
