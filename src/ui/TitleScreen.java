package ui;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import util.KeyHandler;
import util.GameStateManager;

public class TitleScreen {

    GamePanel gp;
    GameStateManager state;
    KeyHandler keyH;
    public int commandNum = 0; //0Start, 1Settings, 2Exit

    public TitleScreen(GamePanel gp, GameStateManager state, KeyHandler keyH) {
        this.gp = gp;
        this.state = state;
        this.keyH = keyH;
    }


    public void update() {
        // Move cursor up
        if (keyH.upPressed) {
            commandNum--;
            if (commandNum < 0) commandNum = 3;
            keyH.upPressed = false;
        }

        // Move cursor down
        if (keyH.downPressed) {
            commandNum++;
            if (commandNum > 3) commandNum = 0;
            keyH.downPressed = false;
        }

        // Select option
        if (keyH.enterPressed) {
            if (commandNum == 0) {
                state.setState(1);
            }
            if (commandNum == 1) {
                state.setState(3);
            }
            if (commandNum == 2) {
                System.exit(0);
            }
            keyH.enterPressed = false;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 48));

        gp.drawGradientBox(g2, 0,0,gp.screenWidth,gp.screenHeight);

        String title = "Shoot Out 2";
        int x = gp.centeredText(title); //Use the method we made in GamePanel
        int y = 150;
        g2.drawString(title, x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 32));

        //use an array so we can do .length when we add new stuff... we'll have to update the if(commandNum) though
        String[] options = {"New Game", "Settings", "Exit"};

        for (int i = 0; i < options.length; i++) {
            x = gp.screenWidth/2 - 100;
            y = 300 + i * 50;

            g2.drawString(options[i], x, y);

            //This is how we draw the cursor
            if (commandNum == i) {
                g2.drawString(">", x - 40, y);
            }
        }
    }
}
