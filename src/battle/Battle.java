package battle;
//We need evolving projectiles, so we use an ArrayList
import java.util.ArrayList;
import entity.*;
import ui.GamePanel;
import util.GameStateManager;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;

public class Battle {
    Player player1;
    Player player2;
    //Do NOT EVER give private GamePanel, that makes a NEW gamepanel                                                                LOOK AT THIS LINE FOR DEBUGGING CADEN
    GamePanel gp;
    private ArrayList<Projectile> activeProjectiles;
    GameStateManager stateManager;
    
    private int left_zone;
    private int right_zone;
    private int down_zone;
    private int up_zone;

    private final double grav = 0.4;
    private final double t_v = 14.0;
    private final int ground = 450;
    private final int buffer = 100; //This is our offscreen buffer

    public Battle(GamePanel gp, GameStateManager stateManager, Player p1, Player p2) {
        this.gp = gp; // Always map the core reference first!
        this.stateManager = stateManager;
        this.player1 = p1;
        this.player2 = p2;
        this.activeProjectiles = new ArrayList<>();

        // Now gp is guaranteed not to be null when reading screen settings
        this.left_zone = -buffer;
        this.right_zone = gp.screenWidth + buffer;
        this.down_zone = gp.screenHeight + buffer;
        this.up_zone = -buffer;
        
        // Link the players back to this battle manager instance so they can use shoot()
        this.player1.setBattle(this);
        this.player2.setBattle(this);
    }

    
    /*                                                      DEBUG SECTION
    Even moving this below this.gp = gp didn't help...
    I'm followign the exact same format as TitelScreen????


    Okay so the problem is: Exception in thread "main" java.lang.NullPointerException: Cannot read field "screenWidth" because "this.gp" is null
    Thsi means this.gp is null, so whatever it's reading, is ending up not sharing with gamepanel.
    In gamepanel, I do this: battle = new Battle(this, stateManager, p1, p2); mimicing: titleScreen = new TitleScreen(this, stateManager, keyH);
    So why is this not working we ask?

    I've imported gamepanel so that's not a problem... it would've thrown a different error anyways
    
    -                                                            SOLUTION:
    declare the variables at the top, then initilaize them in public battle
    */

    public int getGround(){
        return ground;
    }

    public void update() {
        player1.update();
        player2.update();

        updateProjectiles();
        checkBlastZone(player1, player2);
    }

    public void spawnProjectile(double x, double y, double velX, double velY, double damage) {
        activeProjectiles.add(new Projectile(x, y, velX, 0, damage, false));
    }

    private void updateProjectiles() {
        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = activeProjectiles.get(i);
            p.update();

            // --- PLAYER 1 COLLISION ---
            if (p.getHitbox().intersects(player1.getHitbox())) {
                player1.addDamage(p.getDamage());
                p.isDead = true;

                boolean projectileGoingRight = (p.velX > 0);
                if (projectileGoingRight) {
                    player1.addKnockX(player1.getDamage()*1.5);
                } else {
                    player1.addKnockX(player1.getDamage()*-1.5);
                }
                    player1.addKnockY(player1.getDamage()); 
                player1.setGrounded(false);
            }

            // --- PLAYER 2 COLLISION ---
            if (p.getHitbox().intersects(player2.getHitbox())) {
                player2.addDamage(p.getDamage());
                p.isDead = true;

                boolean projectileGoingRight = (p.velX > 0);
                if (projectileGoingRight) {
                    player2.addKnockX(player2.getDamage()*1.5);
                } else {
                    player2.addKnockX(player2.getDamage()*-1.5);
                }
                    player2.addKnockY(player2.getDamage()); 
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

        //Percentages
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("P1: " + (int)player1.getDamage() + "%", 200, 530);
        g2.drawString("P2: " + (int)player2.getDamage() + "%", 1000, 530);
    }

}