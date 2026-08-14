package util;

import ui.GamePanel;

public class GameStateManager {

    public static final int title = 0;
    public static final int select = 1;
    public static final int battle = 2;
    public static final int settings = 3;
    
    private int currentState = title;

    private int subState = 0;

    public GameStateManager(GamePanel gp, KeyHandler keyH) {
    }

    public void setState(int newState) {
        currentState = newState;
    }

    public void setSubState(int newSubState) {
        subState = newSubState;
    }

    public int getState() {
        return currentState; 
    }

    public int getSubState() {
        return subState;
    }
}
