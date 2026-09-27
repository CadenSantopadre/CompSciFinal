package battle;

import java.awt.Rectangle;
import entity.Player;

public class Sword {
    public double x;
    public double y;
    public int width;
    public int height;
    public double damage;
    public boolean isDead;
    
    private final Player owner;
    private final String direction;
    private final int durationFrames;
    private int currentFrame = 0;
    
    // Performance Optimization: Cache a single internal Rectangle instance to prevent memory allocation spikes
    private final Rectangle hitboxCache = new Rectangle();

    public Sword(Player owner, String direction, double damage, int durationFrames) {
        this.owner = owner;
        this.direction = direction;
        this.damage = damage;
        this.durationFrames = durationFrames;
        this.isDead = false;

        // Optimized dimensions based on classic melee fighting frames
        if ("left".equals(direction) || "right".equals(direction)) {
            this.width = 46;  
            this.height = 28; 
        } else {
            this.width = 28;
            this.height = 46;
        }
        updatePosition();
    }

    public void updatePosition() {
        // Safe mapping to player bounding box (assumes standard 32x48 sizing)
        int pWidth = 32;
        int pHeight = 48;

        switch (direction) {
            case "left"  -> { this.x = owner.getX() - this.width; this.y = owner.getY() + (pHeight / 2) - (this.height / 2); }
            case "right" -> { this.x = owner.getX() + pWidth;           this.y = owner.getY() + (pHeight / 2) - (this.height / 2); }
            case "up"    -> { this.x = owner.getX() + (pWidth / 2) - (this.width / 2);  this.y = owner.getY() - this.height; }
            case "down"  -> { this.x = owner.getX() + (pWidth / 2) - (this.width / 2);  this.y = owner.getY() + pHeight; }
        }
        
        // Keep the cached hitbox coordinates synchronized without allocating heap space
        hitboxCache.setBounds((int)x, (int)y, width, height);
    }

    public void update() {
        if (++currentFrame >= durationFrames) {
            isDead = true;
        }
    }

    public Player getOwner() { return owner; }
    public String getDirection() { return direction; }
    public double getDamage() { return damage; }
    public Rectangle getHitbox() { return hitboxCache; } // Ultra-fast return of reference
}
