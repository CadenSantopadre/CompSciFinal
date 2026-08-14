package battle;
//We need evolving projectiles, so we use an ArrayList
import java.util.ArrayList;
import entity.*;
import ui.GamePanel;
import util.GameStateManager;

public class Battle {
    private Player1 player1;
    private Player2 player2;
    private GamePanel gp;
    private ArrayList<Projectile> activeProjectiles;
    private GameStateManager stateManager;
    


    private final double grav = 0.4;
    private final double t_v = 14.0;
    private final int ground = 450;
    private final int buffer = 100; //This is our offscreen buffer
    private final int left_zone = -buffer;
    private final int right_zone = gp.screenWidth +buffer;
    private final int down_zone = gp.screenHeight+buffer;
    private final int up_zone = -buffer;

    public Battle(Player1 p1, Player2 p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.activeProjectiles = new ArrayList<>();
    }

    public void update() {
        player1.update();
        player2.update();

        applyPhysics1(player1);
        applyPhysics2(player2);

        updateProjectiles();
        checkBlastZone(player1, player2);
    }


    //I already regret makign different plaer classes, I should've foudn a workaround
    private void applyPhysics1(Player1 p) {
        if(p.isGrounded==false){
            p.velY += grav;
            if(p.velY > t_v) {
                p.velY = t_v;
            }
        }
            
        p.x += p.velX;
        p.y += p.velY;

        if(p.y >= ground) {
            p.y = ground;
            p.velY = 0;
            p.isGrounded = true;
        }
    }
    private void applyPhysics2(Player2 p) {
        if(p.isGrounded==false){
            p.velY += grav;
            if(p.velY > t_v) {
                p.velY = t_v;
            }
        }
            
        p.x += p.velX;
        p.y += p.velY;

        if(p.y >= ground) {
            p.y = ground;
            p.velY = 0;
            p.isGrounded = true;
        }
    }



    public void spawnProjectile(double x, double y, double velX, double velY, int damage, boolean isDead) {
        activeProjectiles.add(new Projectile(x, y, velX, velY, damage, isDead));
    }

    private void updateProjectiles() {
        //We have to use a reverse for loop because otherwise you get an error.
        //Not sure why, google said this works though

        //Anyways this is gonna get sorta messy so I'll leave more comments
        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = activeProjectiles.get(i);
            p.update();

            if(p.getHitbox().intersects(player1.getHitbox())){ //.intersects is nifty here
                player1.damagePercentage += p.damage; //add damage
                p.isDead = true;

                boolean isRight = (p.velX > 0) ? true:false; //IF p.velX is +, so ->, then isRight is true, othersiwe its false
                if(isRight){
                    player1.velX = player1.damagePercentage*1.5;//Vel scales with damage
                } else{
                    player1.velX = -player1.damagePercentage*1.5;
                }
                player1.velY = -player1.damagePercentage; //Y is more forgiving
                player1.isGrounded = false;
            }
            //Same thign for p2
            if(p.getHitbox().intersects(player2.getHitbox())){ //.intersects is nifty here
                player2.damagePercentage += p.damage; //add damage
                p.isDead = true;

                boolean isRight = (p.velX > 0) ? true:false; //IF p.velX is +, so ->, then isRight is true, othersiwe its false
                if(isRight){
                    player2.velX = player2.damagePercentage*1.5;//Vel scales with damage
                } else{
                    player2.velX = -player2.damagePercentage*1.5;
                }
                player2.velY = -player2.damagePercentage; //Y is more forgiving
                player2.isGrounded = false;
            }
            if (p.isDead) {
                activeProjectiles.remove(i);
            }
        }
    }

    public void checkBlastZone(Player1 p1, Player2 p2) {
        if(p1.x < left_zone || p1.x > right_zone || p1.y < up_zone || p1.y > down_zone){
            stateManager.setState(1);
        }
        if(p2.x < left_zone || p2.x > right_zone || p2.y < up_zone || p2.y > down_zone){
            stateManager.setState(1);
        }
    }

    //Next we add the draw methods
}