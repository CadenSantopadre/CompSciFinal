package entity;
//Literally the only difference here is aPressed <---> leftPressed
import util.KeyHandler;
import java.awt.Rectangle;
import battle.Battle;

//import java.awt.image.BufferedImage; //This lets us use images
//import javax.imageio.ImageIO; //This lets us read the images
//import java.io.IOException;//This lets us find errors I think


public class Player {
    private boolean isP1;
    private int x, y;
    private double velX, velY;
    private int width = 32, height = 48;
    private double damagePercentage = 0.0;
    private boolean isGrounded = false;
    private boolean isRight = true;

    private Battle battle;

    private KeyHandler keyH;
    private Character currentChar;
    private int shootCooldown = 0;
    //private BufferedImage sprite;

    public Player(boolean isP1, int x, int y, KeyHandler keyH, Character currentChar) {
        this.isP1 = isP1;
        this.x = x;
        this.y = y;
        this.keyH = keyH;
        this.currentChar = currentChar;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public boolean getGrounded(){
        return isGrounded;
    }
    public double getDamage(){
        return damagePercentage;
    }
    public double getVelX(){
        return velX;
    }
    public double getVelY(){
        return velY;
    }
    public void setVelY(double set){
        velY = set;
    }
    public void addVelY(double add){
        velY += add;
    }

    public void setBattle(Battle battle){
        this.battle = battle;
    }

    public void update() {
        if(isP1){
            if (keyH.aPressed) {
                velX = -currentChar.getSpeed();
                isRight = false;
            } else if (keyH.dPressed) {
                velX = currentChar.getSpeed();
                isRight = true;
            } else {
                velX = 0;
            }

            if (keyH.wPressed && isGrounded) {
                velY = -currentChar.getJump();
                isGrounded = false;
            }

            if (shootCooldown > 0) shootCooldown--;
            if (keyH.sPressed && shootCooldown == 0) {
                shoot();
            }    
        }
        else{
            if (keyH.leftPressed) {
                velX = -currentChar.getSpeed();
                isRight = false;
            } else if (keyH.rightPressed) {
                velX = currentChar.getSpeed();
                isRight = true;
            } else {
                velX = 0;
            }

            if (keyH.upPressed && isGrounded) {
                velY = -currentChar.getJump();
                isGrounded = false;
            }

            if (shootCooldown > 0) shootCooldown--;
            if (keyH.downPressed && shootCooldown == 0) {
                shoot();
            }
        }
    }

    private void shoot() {
        int direction = isRight ? 1 : -1;
        int projectileX = x + (width / 2) + (direction * 50); //This way it won't crash into player
        int projectileY = y + (height / 2);

        battle.spawnProjectile(projectileX, projectileY, direction * currentChar.getSpeed() * 1.6, 0, 10);
        shootCooldown = currentChar.getFireRate();
    }

    public Rectangle getHitbox() {
        return new Rectangle(x, y, width, height);
    }
}
