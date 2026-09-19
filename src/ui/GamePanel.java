package ui;

import javax.swing.JPanel; //lets me use jpanel

import java.awt.Color; //lets me use colors... like Color.BLUE
import java.awt.Dimension; //lets me use screenHeight and screenWidth
import java.awt.Graphics2D;
import java.awt.Graphics; //G and G2D are real helpful here
import java.awt.GradientPaint; //Lets me do a cool background
import java.awt.BasicStroke; //Used for making lines and stuff

import util.*;
import entity.*;
import battle.Battle;

public class GamePanel extends JPanel implements Runnable {
    private Thread gameThread;
    private GameStateManager stateManager;
    private Graphics2D g2;
    private KeyHandler keyH = new KeyHandler();
    private TitleScreen titleScreen;
    private CharSelect charSelect;
    private Battle battle;
    private Player p1;
    private Player p2;

    public int screenWidth = 1280;
    public int screenHeight = 720;

    public GamePanel() {
        setPreferredSize(new Dimension(screenWidth, screenHeight)); //idk why we need a "dimension" object but whatever
        setBackground(Color.BLACK); //This is where awt.color comes in
        setFocusable(true);
        addKeyListener(keyH);

        //I'll do things like party = new Party(); here and init them

        stateManager = new GameStateManager(this, keyH);

        titleScreen = new TitleScreen(this, stateManager, keyH);

        charSelect = new CharSelect(this, stateManager, keyH);

        //DONT put battle here, look below                                                          LOOK BELOW CADEN
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        final int FPS = 30;
        final double frameTime = 1_000_000_000.0 / FPS; //Good to add a .0 at the end so it does a double calculation

        long lastTime = System.nanoTime(); //use long since it won't be negative
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / frameTime;
            lastTime = now;

            while (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            try {
                Thread.sleep(1); // prevents 100% CPU usage
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        int state = stateManager.getState();
        /*
        Title = 0
        CharSelect = 1
        Battle = 2
        Settings = 3
        */
       switch (state) {
            case 0:
            titleScreen.update();
            break;

            case 1:
            charSelect.update();
            break;

            case 2:
                if (battle == null) {
                    p1 = new Player(true, 200, 300, keyH, charSelect.p1);
                    p2 = new Player(false, 1000, 300, keyH, charSelect.p2);
                    battle = new Battle(this, stateManager, p1, p2);
                    p1.setBattle(battle);
                    p2.setBattle(battle);
                }
                battle.update();
            break;
            /*
            case 3:
            settings.update();
            break;*/
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g2 = (Graphics2D) g; //Just casting it works I guess, dunno why we need to do that but

        int state = stateManager.getState();
        switch (state) {
            case 0:
            titleScreen.draw(g2);
            break;

            case 1:
            charSelect.draw(g2);
            break;
            
            
            case 2:
            if (battle != null) {
                battle.draw(g2);
            }
            break;
            /*
            case 3:
            settings.draw(g2);
            break;*/
        }
    }
    //And here's where I'll put any extra "global" stuff
    
    public void startNewMatch() {
        // 1. Create fresh players so their positions and damage percentages go back to default
        p1 = new Player(true, 200, 300, keyH, charSelect.p1);
        p2 = new Player(false, 1000, 300, keyH, charSelect.p2);
        battle = new Battle(this, stateManager, p1, p2);
        p1.setBattle(battle);
        p2.setBattle(battle);
        battle.update();
    }

    
    public void drawGradientBox(Graphics2D g2, int x, int y, int w, int h) {
        //left
        GradientPaint left = new GradientPaint(x, y, Color.BLACK, x, y + h, Color.BLUE);

        //right
        GradientPaint right = new GradientPaint(x + w, y, Color.BLACK, x + w, y + h, Color.BLUE);

        //actually drawing left half
        g2.setPaint(left);
        g2.fillRect(x, y, w / 2, h);

        //actually drawing right half
        g2.setPaint(right);
        g2.fillRect(x + w / 2, y, w / 2, h);

        //border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5)); 
        g2.drawRect(x, y, w, h);
    }

    public void drawGradientBox(Graphics2D g2, int x, int y, int w, int h, Color top, Color bottom) {
        //left
        GradientPaint left = new GradientPaint(x, y, top, x, y + h, bottom);

        //right
        GradientPaint right = new GradientPaint(x + w, y, top, x + w, y + h, bottom);

        //actually drawing left half
        g2.setPaint(left);
        g2.fillRect(x, y, w / 2, h);

        //actually drawing right half
        g2.setPaint(right);
        g2.fillRect(x + w / 2, y, w / 2, h);

        //border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(5)); 
        g2.drawRect(x, y, w, h);
    }
    
    public int centeredText(String string){
        int px = 0;
        px = this.screenWidth/2 - g2.getFontMetrics().stringWidth(string)/2;
        return px;
    }
}
