package com.mycompany.app.Characters;

import com.almasb.fxgl.dsl.FXGL;

import com.almasb.fxgl.texture.Texture;
import com.mycompany.app.Power.*;
import com.mycompany.app.Save.Data;
import com.mycompany.app.Controller.GameFactory;
import com.mycompany.app.Events.Notification.NotificationListener;

import javafx.geometry.Point2D;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Player extends Character {

    protected double points = 0;
    protected PowerType powerType = null;
    protected Power activePower;
    protected PlayerTypes playerType;
    protected double lastShot = System.currentTimeMillis();
    protected NotificationListener notificationListener;
    protected double fireRate = 250;
    protected long lastUsePower = 0;
    protected boolean isInvincible = false;

    public Player(PlayerTypes playerType, int speed, int life, NotificationListener notificationListener) {
        super("player/" + playerType.name() + ".png", 34, 32, speed, life);
        this.playerType = playerType;
        this.notificationListener = notificationListener;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        this.addPlayerName();
    }

    public void shotProjectile(GameFactory gameFactory) {
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

        gameFactory.newBullet(this.playerType, origin, direction, "bullet1", 400);
    }

    public void hit() {
        this.setActivePower();
        this.points += 10;
    }

    @Override
    public int damage() {
        if(!isInvincible){
            this.notificationListener.fireEvent(Color.RED, "Você tem " + this.life + " ponto de vida restante.");
            return --this.life;
        }
        return this.life;
    }

    public boolean canShot() {
        double diffFromLastShot = System.currentTimeMillis() - this.lastShot;
        return diffFromLastShot > this.fireRate;
    }

    protected void setActivePower() {
        Random rand = new Random();

        if (this.getPoints() > 0 && this.getPoints()%100 == 0) {
            List<PowerType> givenList = Arrays.asList(PowerType.SPEED, PowerType.SPEEDSHOT, PowerType.INVINCIBLE);

            this.powerType = givenList.get(rand.nextInt(givenList.size()));
            this.notificationListener.fireEvent(Color.BISQUE, "Você ganhou um novo poder: " + this.powerType);
            usePower();
        }
    }

    public void usePower() {
        if (powerType != null) {
            this.lastUsePower = System.currentTimeMillis();
            switch (powerType) {
                case SPEED:
                    activePower = new MoveSpeed();
                    this.setPlayerData(activePower.use(this.getPlayerData()));
                    break;
                case SPEEDSHOT:
                    activePower = new SpeedShot();
                    this.setPlayerData(activePower.use(this.getPlayerData()));
                    break;
                case INVINCIBLE:
                    activePower = new Invincible();
                    this.setPlayerData(activePower.use(this.getPlayerData()));
                    break;
            }
        }
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        this.resetPlayer();
    }

    public double getPoints() {
        return this.points;
    }

    public Data getPlayerData() {
        return new Data(this.points, this.speed, this.powerType, this.lastShot, this.life, this.fireRate, this.isInvincible);
    }

    public void setPlayerData(Data data) {
        this.powerType = data.getActivePower();
        this.lastShot = data.getLastShot();
        this.speed = data.getSpeed();
        this.life = data.getLife();
        this.points = data.getPoints();
        this.fireRate = data.getFireRate();
        this.isInvincible = data.getInvincibility();
    }

    private void addPlayerName() {
        Texture texture = new Texture(FXGL.image("indicators/" + this.playerType.name() + ".png"));

        StackPane stackPane = new StackPane(texture);
        stackPane.setTranslateY(-32);
        stackPane.setTranslateX(2);

        this.entity.getViewComponent().addChild(stackPane);
    }
    private void resetPlayer(){
        if((this.activePower != null) && (System.currentTimeMillis() - this.lastUsePower > 5000)){
            this.activePower = null;
            this.setPlayerData(new Data(this.points, 200, null, this.lastShot, 2, 250, false));
        }
    }
}


