package entity;

public class Character {
    private String imgPath;
    private String name;
    private double speed;
    private int fireRate;
    private double jump;
    private double weight;
    private boolean unlocked;
    private boolean melee;

    public Character(String imgPath, String name, double speed, int fireRate, double jump, double weight, boolean unlocked, boolean melee){
        this.imgPath = imgPath;
        this.name = name;
        this.speed = speed;
        this.fireRate = fireRate;
        this.jump = jump;
        this.weight = weight;
        this.unlocked = unlocked;
        this.melee = melee;   
    }

    public String getImgPath(){
        return imgPath;
    }
    public String getName(){
        return name;
    }
    public double getSpeed(){
        return speed;
    }
    public int getFireRate(){
        return fireRate;
    }
    public double getJump(){
        return jump;
    }
    public double getWeight(){
        return weight;
    }
    public boolean getUnlocked(){
        return unlocked;
    }
    public boolean getMelee(){
        return melee;
    }
}
