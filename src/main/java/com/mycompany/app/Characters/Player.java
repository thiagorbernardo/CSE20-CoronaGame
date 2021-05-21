package com.mycompany.app.Characters;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.mycompany.app.Power.ActivePower;
import com.mycompany.app.Save.Data;
import com.mycompany.app.Controller.GameFactory;
import com.mycompany.app.Events.Notification.NotificationListener;
import com.mycompany.app.Power.PowerType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Player extends Character {

    protected double points = 0;
    protected PowerType activePower = null;
    protected ActivePower power;
    protected PlayerTypes playerType;
    protected double lastShot = System.currentTimeMillis();
    protected NotificationListener notificationListener;

    public Player(PlayerTypes playerType, int speed, int life, NotificationListener notificationListener) {
        super("player/" + playerType.name() + ".png", 34, 32, speed, life);
        this.playerType = playerType;
        this.notificationListener = notificationListener;
    }

    public Entity shotProjectile(GameFactory gameFactory) {
        Point2D origin = this.entity.getCenter();

        Point2D direction;

        switch (this.activeDirection) {
            case RIGHT:
                direction = origin.subtract(-FXGL.getAppWidth(), origin.getY());
                break;
            case LEFT:
                direction = origin.subtract(FXGL.getAppWidth(), origin.getY());
                break;
            case UP:
                direction = origin.subtract(origin.getX(), FXGL.getAppHeight());
                break;
            default:
                direction = origin.subtract(origin.getX(), -FXGL.getAppHeight());
                break;
        }

        this.lastShot = System.currentTimeMillis();

        return gameFactory.newBullet(this.playerType, origin, direction, "bullet1", 400);
    }

    public void hit() {
        this.setActivePower();
        this.points += 10;
    }

    @Override
    public int damage() {
        this.notificationListener.fireEvent(Color.RED, "Você tem " + this.life + " ponto de vida restante.");
        return super.damage();
    }

    public boolean canShot() {
        double diffFromLastShot = System.currentTimeMillis() - this.lastShot;
        return diffFromLastShot > 250;
    }

    protected void setActivePower() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(10);
        System.out.println(randomNumber == 2);

        if (randomNumber == 2) {
//            List<PowerType> givenList = Arrays.asList(PowerType.SPEED, PowerType.SPEEDSHOT, PowerType.PURGE);
//
//            this.activePower = givenList.get(rand.nextInt(givenList.size()));
//            System.out.println("New power");
//            this.notificationListener.fireEvent(Color.BISQUE, "Você ganhou um novo poder: " + this.activePower);
            usePower();
        }
    }

    public void usePower() {
//        this.setPlayerData(power.use(activePower));
        this.activePower = null;
    }

    public double getPoints(){
        return this.points;
    }

    public Data getPlayerData(){
        return new Data(this.points, this.speed, this.activePower, this.lastShot, this.life);
    }

    public void setPlayerData(Data data) {
        this.activePower = data.getActivePower();
        this.lastShot = data.getLastShot();
        this.speed = data.getSpeed();
        this.life = data.getLife();
        this.points = data.getPoints();
    }

    public PlayerTypes getPlayerType() {
        return this.playerType;
    }
}
