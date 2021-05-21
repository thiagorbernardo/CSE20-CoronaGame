package com.mycompany.app.Save;


import com.mycompany.app.Power.PowerType;

public class Data {
    private double points;
    private int speed;
    private PowerType activePower;
    private double lastShot;
    private int life;

    public Data(double points, int speed, PowerType activePower, double lastShot, int life) {
        this.points = points;
        this.speed = speed;
        this.activePower = activePower;
        this.lastShot = lastShot;
        this.life = life;
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

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setLastShot(int lastShot) {
        this.lastShot = lastShot;
    }

}
