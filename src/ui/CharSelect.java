package ui;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import util.KeyHandler;
import util.GameStateManager;
import entity.Character;


public class CharSelect {
    GamePanel gp;
    GameStateManager state;
    KeyHandler keyH;
    public int commandNum1 = 0;
    public int commandNum2 = 0;
    
    //character roster
    /*
    public String imgPath;
    public String name;
    public double speed;
    public int HP;
    public int fireRate;
    public double jump;
    public double weight;
    */
    public static final Character[] chars = {
      //new Character(        "ImgPath"    ,      "Name"   ,       Speed,    fireRate,  Jump,      Weight,      unlocked),
        new Character("NO_IMG_YET", "Shooter", 3.0, 5, 2.0, 2.0, true),
        new Character("NO_IMG_YET","Clubber", 2.0, 10, 2.0, 2.0, false)
    };

    public Character p1;
    public Character p2;

    public boolean p1Selected = false;
    public boolean p2Selected = false;
    
    public CharSelect(GamePanel gp, GameStateManager state, KeyHandler keyH) {
        this.gp = gp;
        this.state = state;
        this.keyH = keyH;
    }

    public void update() {
        //P1 - uses WASD/space
        if(keyH.wPressed){
            commandNum1--;
            if(commandNum1 < 0) commandNum1 = chars.length-1;
            keyH.wPressed = false;
            p1Selected=false;
        }
        if(keyH.sPressed){
            commandNum1++;
            if(commandNum1 >= chars.length) commandNum1 = 0;
            keyH.sPressed = false;
            p1Selected=false;
        }
        if(keyH.spacePressed){
            p1 = chars[commandNum1];
            p1Selected = true;
        }

        //P2 - uses arrow keys/enter
        if(keyH.upPressed){
            commandNum2--;
            if(commandNum2 < 0) commandNum2 = chars.length-1;
            keyH.upPressed = false;
            p2Selected = false;
        }
        if(keyH.downPressed){
            commandNum2++;
            if(commandNum2 >= chars.length) commandNum2 = 0;
            keyH.downPressed = false;
            p2Selected = false;
        }
        if(keyH.enterPressed){
            p2 = chars[commandNum2];
            p2Selected = true;
        }

        if (p1Selected && p2Selected) {
            state.setState(2);
            
            p1Selected = false;
            p2Selected = false;
        }

    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 48));

        gp.drawGradientBox(g2, 0,0,gp.screenWidth,gp.screenHeight);

        String title = "Character Select";
        int x = gp.centeredText(title);
        int y = 50;
        g2.drawString(title, x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 32));

        //Drawing p1 characters
        for (int i = 0; i < chars.length; i++) {
            x = 50;
            y = 100 + i * 50;
            //Give Dark Grey if they aren't unlocked
            if(chars[i].unlocked){
                g2.setColor(Color.WHITE);
            }
            else {
                g2.setColor(Color.DARK_GRAY);
            }

            g2.drawString(chars[i].name, x, y);

            if (commandNum1 == i) {
                g2.drawString(">", x - 40, y);
                if(p1Selected) {
                    g2.drawString("[Selected]", x + 150, y); // Adjust X offset as needed
                }
            }
        }

        //Drawing p2 characters
        for (int i = 0; i < chars.length; i++) {
            x = 1100;
            y = 100 + i * 50;
            //Give Dark Grey if they aren't unlocked
            if(chars[i].unlocked){
                g2.setColor(Color.WHITE);
            }
            else {
                g2.setColor(Color.DARK_GRAY);
            }

            g2.drawString(chars[i].name, x, y);

            if (commandNum2 == i) {
                g2.drawString(">", x - 40, y);
                if(p2Selected) {
                    g2.drawString("[Selected]", x + 150, y); // Adjust X offset as needed
                }
            }
        }
    }

    /*
    Eventually we'll make a method that shows the name and character side by side...
    Then use that in the for loops instead of g2.drawString
    */
}
