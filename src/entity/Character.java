package entity;

public class Character {
    public String imgPath;
    public String name;
    public double speed;
    public int fireRate;
    public double jump;
    public double weight;
    public boolean unlocked;

    public Character(String imgPath, String name, double speed, int fireRate, double jump, double weight, boolean unlocked){
        this.imgPath = imgPath;
        this.name = name;
        this.speed = speed;
        this.fireRate = fireRate;
        this.jump = jump;
        this.weight = weight;
        this.unlocked = unlocked;   
    }
}
