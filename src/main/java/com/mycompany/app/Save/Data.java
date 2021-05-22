package com.mycompany.app.Save;


import com.mycompany.app.Power.PowerType;

public class Data {
    private double points;
    private int speed;
    private PowerType activePower;
    private long lastShot;
    private int life;
    private double fireRate;
    private boolean isInvincible;

    public Data(double points, int speed, PowerType activePower, double lastShot, int life, double fireRate, boolean isInvincible) {
        this.points = points;
        this.speed = speed;
        this.activePower = activePower;
        this.lastShot = (long) lastShot;
        this.life = life;
        this.fireRate = fireRate;
        this.isInvincible = isInvincible;
    }

    public double getPoints() {
        return this.points;
    }

    public int getSpeed() {
        return this.speed;
    }

    public PowerType getActivePower() {
        return this.activePower;
    }

    public long getLastShot() {
        return this.lastShot;
    }

    public int getLife() {
        return this.life;
    }

    public double getFireRate() {
        return this.fireRate;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setFireRate(double fireRate){
        this.fireRate = fireRate;
    }

    public void setIsInvincible(boolean isinvincible){
        this.isInvincible = isinvincible;
    }

    public boolean getInvincibility() {
        return this.isInvincible;
    }
}
