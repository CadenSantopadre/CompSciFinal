package entity;

import java.awt.Rectangle;

public class Entity {
    //Position and stuff
    public double x;
    public double y;
    public double velX;
    public double velY;

    public int width;
    public int height;

    //SSB stuff
    public double dmgPer = 0.0;
    public boolean isGrounded = false;
    public boolean isRight = true;

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}