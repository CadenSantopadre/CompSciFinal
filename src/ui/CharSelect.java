package ui;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import util.KeyHandler;
import util.GameStateManager;
import entity.Character;
import map.Tilemap;


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
        //imgPath is there, but we don't use it yet, right now 
        new Character("src\\res\\CCS250RD.jpg", "Shooter", 10.0, 50, 10.0, 1.0, true, false),
        new Character("src\\res\\CCS400BL.jpg","Heavy", 5.0, 100, 10.0, 1.0, false, false),
        new Character("no img yet", "Sword", 10.0, 10, 10.0, 1.0, true, true),
    };

    public Character p1;
    public Character p2;

    public boolean p1Selected = false;
    public boolean p2Selected = false;
    private final Tilemap[] stages = Tilemap.getStages();
    private int stageChoice = 0;
    private boolean choosingStage = false;
    public Tilemap selectedStage = stages[0];
    
    public CharSelect(GamePanel gp, GameStateManager state, KeyHandler keyH) {
        this.gp = gp;
        this.state = state;
        this.keyH = keyH;
    }

    public void update() {
        if (choosingStage) {
            if (keyH.wPressed || keyH.upPressed) {
                stageChoice = (stageChoice + stages.length - 1) % stages.length;
                keyH.wPressed = false;
                keyH.upPressed = false;
            }
            if (keyH.sPressed || keyH.downPressed) {
                stageChoice = (stageChoice + 1) % stages.length;
                keyH.sPressed = false;
                keyH.downPressed = false;
            }
            if (keyH.spacePressed || keyH.enterPressed) {
                selectedStage = stages[stageChoice];
                keyH.spacePressed = false;
                keyH.enterPressed = false;
                choosingStage = false;
                gp.startNewMatch(selectedStage);
                state.setState(2);
            }
            return;
        }

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
            keyH.spacePressed = false;
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
            keyH.enterPressed = false;
        }

        if (p1Selected && p2Selected) {
            choosingStage = true;
            stageChoice = 0;
            p1Selected = false;
            p2Selected = false;
        }

    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 48));

        gp.drawGradientBox(g2, 0,0,gp.screenWidth,gp.screenHeight);

        // Inside your draw loop:
        if (choosingStage) {
            int padding = 12;
            int startY = 50;

            int height = gp.screenHeight - 2*startY;
            int segHeight = height / stages.length; 
            int maxCols = 0;
            int maxRows = 0;
            for (Tilemap stage : stages) {
                maxCols = Math.max(maxCols, stage.getColCount());
                maxRows = Math.max(maxRows, stage.getRowCount());
            }
            int availableWidth = gp.screenWidth - 2 * padding;
            int availableHeight = segHeight - 2 * padding;
            int previewScale = Math.max(1, Math.min(12,
                    Math.min(availableWidth / maxCols, availableHeight / maxRows)));

             for (int i = 0; i < stages.length; i++) {
                int mapCols = stages[i].getColCount();
                int mapRows = stages[i].getRowCount();
                
                int previewWidth = mapCols * previewScale;
                int previewHeight = mapRows * previewScale;

                // Horizontally center
                int previewX = (gp.screenWidth - previewWidth) / 2;
                
                // Vertically center EACH preview inside its designated "slice" of screen height
                // This distributes them perfectly up and down the screen!
                int sliceCenterY = startY + (i * segHeight) + (segHeight / 2);
                int previewY = sliceCenterY - (previewHeight / 2);

                // 5. Selection Highlight Box
                if (i == stageChoice) {

                    g2.setColor(new Color(255, 255, 0, 30));
                    g2.fillRect(previewX - padding, previewY - padding, previewWidth + (padding * 2), previewHeight + (padding * 2));

                    g2.setColor(Color.YELLOW);
                    g2.drawRect(previewX - padding, previewY - padding, previewWidth + (padding * 2), previewHeight + (padding * 2));
                }

                // 6. Draw
                stages[i].drawPreview(g2, previewX, previewY, previewScale);
            }
            return;
        }



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
            if(chars[i].getUnlocked()){
                g2.setColor(Color.WHITE);
            }
            else {
                g2.setColor(Color.DARK_GRAY);
            }

            g2.drawString(chars[i].getName(), x, y);

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
            if(chars[i].getUnlocked()){
                g2.setColor(Color.WHITE);
            }
            else {
                g2.setColor(Color.DARK_GRAY);
            }

            g2.drawString(chars[i].getName(), x, y);

            if (commandNum2 == i) {
                g2.drawString(">", x - 40, y);
                if(p2Selected) {
                    g2.drawString("[Selected]", x + 150, y); // Adjust X offset as needed
                }
            }
        }
    }
}
