package battle;
//We need evolving projectiles, so we use an ArrayList
import java.util.ArrayList;
import entity.*;
import ui.GamePanel;


public class Battle {
    private Player1 player1;
    private Player2 player2;
    private GamePanel gp;
    private ArrayList<Projectile> activeProjectiles;
    


    private final double grav = 0.4;
    private final double t_v = 14.0;
    private final double air_rest = 0.95;
    private final int ground = 450;
    private final int buffer = 100; //This is our offscreen buffer

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
        checkBlastZones();
    }

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



    public void spawnProjectile(double x, double y, double velX, double velY, int damage, boolean isDead) {
        activeProjectiles.add(new Projectile(x, y, velX, velY, damage, isDead));
    }

    private void updateProjectiles() {
        //We have to use a reverse for loop because otherwise you get an error.
        //Not sure why, google said this works though
        for (int i = activeProjectiles.size() - 1; i >= 0; i--) {
            Projectile p = activeProjectiles.get(i);
            p.update();
            if (p.isDead) {
                activeProjectiles.remove(i);
            }
        }
    }
}
