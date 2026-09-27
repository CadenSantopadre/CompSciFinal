package battle;
//We need evolving projectiles, so we use an ArrayList
import java.util.ArrayList;
import entity.*;
import ui.GamePanel;
import util.GameStateManager;
import map.Tilemap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;

public class Battle {
    Player player1;
    Player player2;
    //Do NOT EVER give private GamePanel, that makes a NEW gamepanel                                                                LOOK AT THIS LINE FOR DEBUGGING CADEN
    GamePanel gp;
    private ArrayList<Projectile> activeProjectiles;
    private ArrayList<Sword> activeSwords;
    GameStateManager stateManager;
    
    private int left_zone;
    private int right_zone;
    private int down_zone;
    private int up_zone;

    private final int buffer = 100; //This is our offscreen buffer
    private final Tilemap tilemap;

    public Battle(GamePanel gp, GameStateManager stateManager, Player p1, Player p2, Tilemap tilemap) {
        this.gp = gp; // Always map the core reference first!
        this.stateManager = stateManager;
        this.player1 = p1;
        this.player2 = p2;
        this.tilemap = tilemap;
        this.activeProjectiles = new ArrayList<>();
        this.activeSwords = new ArrayList<>();

        // Now gp is guaranteed not to be null when reading screen settings
        this.left_zone = -buffer;
        this.right_zone = gp.screenWidth + buffer;
        this.down_zone = gp.screenHeight + buffer;
        this.up_zone = -buffer;
        
        // Link the players back to this battle manager instance so they can use shoot()
        this.player1.setBattle(this);
        this.player2.setBattle(this);
    }

    public int getGround(){
        return gp.screenHeight;
    }

    public Tilemap getTilemap() {
        return tilemap;
    }

    public int getScreenWidth() {
        return gp.screenWidth;
    }

    public int getScreenHeight() {
        return gp.screenHeight;
    }

    public boolean collidesWithTilemap(java.awt.Rectangle hitbox) {
        for (int row = 0; row < tilemap.getRowCount(); row++) {
            for (int col = 0; col < tilemap.getColCount(); col++) {
                if (tilemap.isTileSolid(col, row)
                        && hitbox.intersects(tilemap.getTileBounds(col, row, gp.screenWidth, gp.screenHeight))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void update() {
        player1.update();
        player2.update();

        updateProjectiles();
        updateSwords();
        checkBlastZone(player1, player2);
    }

    public void spawnProjectile(double x, double y, double velX, double velY, double damage) {
        activeProjectiles.add(new Projectile(x, y, velX, 0, damage, false));
    }

    public void spawnSwordArc(Player attacker, String direction, double damage, int durationFrames) {
        activeSwords.add(new Sword(attacker, direction, damage, durationFrames));
    }

    private void updateSwords() {
        // Reverse for-loop is mandatory for safe concurrent removal from ArrayLists
        for (int i = activeSwords.size() - 1; i >= 0; i--) {
            Sword s = activeSwords.get(i);
            
            s.updatePosition(); // Keeps sword locked seamlessly onto moving players
            s.update();

            // 1. Check if Player 2 swung a sword hitting Player 1
            if (s.getOwner() == player2 && s.getHitbox().intersects(player1.getHitbox())) {
                executeHit(player1, player2, s);
                s.isDead = true;
            }
            // 2. Check if Player 1 swung a sword hitting Player 2
            else if (s.getOwner() == player1 && s.getHitbox().intersects(player2.getHitbox())) {
                executeHit(player2, player1, s);
                s.isDead = true;
            }

            if (s.isDead) {
                activeSwords.remove(i);
            }
        }
    }

    // Clean helper method to completely eliminate code duplication
    private void executeHit(Player target, Player attacker, Sword weapon) {
        target.addDamage(weapon.getDamage() * attacker.getChar().getFireRate() * 0.05 * (Math.random() + 0.01));
        
        double knockbackMultiplier = "left".equals(weapon.getDirection()) ? -1.5 : 1.5;
        target.addKnockX(target.getDamage() * knockbackMultiplier);
        target.addKnockY(target.getDamage() * -1.0); 
        target.setGrounded(false);
    }


    private void updateProjectiles() {
        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = activeProjectiles.get(i);
            p.update();

            // --- PLAYER 1 COLLISION ---
            if (p.getHitbox().intersects(player1.getHitbox())) {
                player1.addDamage(p.getDamage() * player2.getChar().getFireRate() * 0.05 * (Math.random()+0.01));
                p.isDead = true;

                boolean projectileGoingRight = (p.velX > 0);
                if (projectileGoingRight) {
                    player1.addKnockX(player1.getDamage()*1.5);
                } else {
                    player1.addKnockX(player1.getDamage()*-1.5);
                }
                    player1.addKnockY(player1.getDamage()*-1); 
                player1.setGrounded(false);
            }

            // --- PLAYER 2 COLLISION ---
            if (p.getHitbox().intersects(player2.getHitbox())) {
                player2.addDamage(p.getDamage() * player1.getChar().getFireRate() * 0.05 * (Math.random()+0.01));
                p.isDead = true;

                boolean projectileGoingRight = (p.velX > 0);
                if (projectileGoingRight) {
                    player2.addKnockX(player2.getDamage()*1.5);
                } else {
                    player2.addKnockX(player2.getDamage()*-1.5);
                }
                    player2.addKnockY(player2.getDamage()*-1); 
                player2.setGrounded(false);
            }

            // Remove dead or out of bounds projectiles
            if (p.isDead) {
                activeProjectiles.remove(i);
            }
        }
    }


    public void checkBlastZone(Player p1, Player p2) {
        if(p1.getX() < left_zone || p1.getX() > right_zone || p1.getY() < up_zone || p1.getY() > down_zone){
            stateManager.setState(1);
        }
        if(p2.getX() < left_zone || p2.getX() > right_zone || p2.getY() < up_zone || p2.getY() > down_zone){
            stateManager.setState(1);
        }
    }

    //Next we add the draw methods
    public void draw(Graphics2D g2){
        //Make a sky/black bottom
        gp.drawGradientBox(g2, 0, 0, gp.screenWidth, gp.screenHeight, Color.BLUE, Color.BLACK);
        tilemap.draw(g2, gp.screenWidth, gp.screenHeight);

        //Then make player 1
        g2.setColor(Color.RED);
        g2.fillRect(player1.getX(), player1.getY(), 32, 48);
        //p2
        g2.setColor(Color.BLUE);
        g2.fillRect(player2.getX(), player2.getY(), 32, 48);

        g2.setColor(Color.YELLOW);
        //No reverse for loop here for some reason??????
        for (int i = 0; i < activeProjectiles.size(); i++) {
            Projectile p = activeProjectiles.get(i);
            g2.fillRect((int)p.x, (int)p.y, p.width, p.height);
        }
        for (int i = 0; i < activeSwords.size(); i++) {
            Sword s = activeSwords.get(i);
            
            // Draw semi-transparent combat slash area
            g2.setColor(new Color(255, 230, 100, 110)); 
            g2.fillRect((int)s.x, (int)s.y, s.width, s.height);
            
            // Hard weapon slash line edge
            g2.setColor(Color.ORANGE);
            g2.drawRect((int)s.x, (int)s.y, s.width, s.height);
        }


        //Percentages
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("P1: " + (int)player1.getDamage() + "%", 200, 530);
        g2.drawString("P2: " + (int)player2.getDamage() + "%", 1000, 530);
    }

}