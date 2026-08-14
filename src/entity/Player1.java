package entity;
//Literally the only difference here is aPressed <---> leftPressed
import util.KeyHandler;
import java.awt.Rectangle;

import java.awt.image.BufferedImage; //This lets us use images
import javax.imageio.ImageIO; //This lets us read the images
import java.io.IOException;//This lets us find errors I think


public class Player1 {
    public double x, y;
    public double velX, velY;
    public int width = 32, height = 48;
    public double damagePercentage = 0.0;
    public boolean isGrounded = false;
    public boolean isRight = true;

    private KeyHandler keyH;
    private Character currentChar;
    private int shootCooldown = 0;
    public BufferedImage sprite;

    public Player1(double x, double y, KeyHandler kh, Character currentChar) {
        this.x = x;
        this.y = y;
        loadSprite(); 
        this.currentChar = currentChar;
    }

    private void loadSprite() {
        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream(currentChar.imgPath));
        } catch(IOException e) {
            System.out.println("Error loading sprite for:");
            System.out.print(currentChar.name);
        }
    }

    public void update() {
        if (keyH.aPressed) {
            velX = -currentChar.speed;
            isRight = false;
        } else if (keyH.dPressed) {
            velX = currentChar.speed;
            isRight = true;
        } else {
            velX = 0;
        }

        if (keyH.wPressed && isGrounded) {
            velY = -12;
            isGrounded = false;
        }

        if (shootCooldown > 0) shootCooldown--;
        if (keyH.spacePressed && shootCooldown == 0) {
            shoot();
        }
    }

    private void shoot() {
        shootCooldown = currentChar.fireRate;
    }
    public Rectangle getHitbox() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
