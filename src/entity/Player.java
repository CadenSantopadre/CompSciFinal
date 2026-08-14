package entity;

import util.KeyHandler;

public class Player extends Entity {//extends Entity means it inherits stuff from Entity 
    private KeyHandler keyH;
    private int shootCooldown = 0;

    public Player(double x, double y, KeyHandler kh) {
        super(x,y,32,48); //using super with the extend entity part
    }

}
