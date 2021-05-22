package com.mycompany.app.Save;


import com.mycompany.app.Power.PowerType;

public class Data {
    private double points;
    private int speed;
    private PowerType activePower;
    private long lastShot;
    private int life;
    private double fireRate;

    public Data(double points, int speed, PowerType activePower, double lastShot, int life, double fireRate) {
        this.points = points;
        this.speed = speed;
        this.activePower = activePower;
        this.lastShot = (long) lastShot;
        this.life = life;
        this.fireRate = fireRate;
    }

    public double getPoints() {
        return points;
    }

    public int getSpeed() {
        return speed;
    }

    public PowerType getActivePower() {
        return activePower;
    }

    public double getLastShot() {
        return lastShot;
    }

    public int getLife() {
        return life;
    }

    public double getFireRate() {
        return fireRate;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setFireRate(double fireRate){
        this.fireRate = fireRate;
    }



}
