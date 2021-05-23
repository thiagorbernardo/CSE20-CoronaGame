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

    private StackPane playerShield;

    /**
     * Constructor
     * @param playerType type of player (P1, P2)
     * @param speed init speed of player
     * @param life starting life
     * @param notificationListener listener to emit notifications
     */
    public Player(PlayerTypes playerType, int speed, int life, NotificationListener notificationListener) {
        super("player/" + playerType.name() + ".png", 34, 32, speed, life);
        this.playerType = playerType;
        this.notificationListener = notificationListener;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        this.addPlayerName();

        Texture texture = new Texture(FXGL.image("indicators/shield.png"));
        texture.setScaleX(1.2);
        texture.setScaleY(1.2);
        this.playerShield = new StackPane(texture);
    }

    public List<Point2D> shotProjectile() {
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

        return Arrays.asList(origin, direction);
    }

    /**
     * Earn points
     */
    public void hit() {
        this.points += 10;
        this.setActivePower();
    }

    /**
     * Damaging player and verifying invincibility
     * @return new life of player
     */
    @Override
    public int damage() {
        if (!isInvincible) {
            if (--this.life == 0)
                this.notificationListener.fireEvent(Color.RED, this.playerType.name() +  " morreu!");
        }
        return this.life;
    }

    /**
     * Check if player can shot
     * @return a boolean
     */
    public boolean canShot() {
        double diffFromLastShot = System.currentTimeMillis() - this.lastShot;
        return diffFromLastShot > this.fireRate;
    }

    /**
     * Logic to verify if a new power is available
     */
    protected void setActivePower() {
        Random rand = new Random();

        if (this.getPoints() > 0 && this.getPoints() % 100 == 0) {
            List<PowerType> givenList = Arrays.asList(PowerType.MOVESPEED, PowerType.SPEEDSHOT, PowerType.INVINCIBLE);

            this.powerType = givenList.get(rand.nextInt(givenList.size()));
            usePower();
        }
    }

    /**
     * Polymorphism of powers
     */
    public void usePower() {
        if (powerType != null) {
            this.lastUsePower = System.currentTimeMillis();
            switch (powerType) {
                case MOVESPEED:
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
                    this.entity.getViewComponent().addChild(this.playerShield);
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

    public PowerType getPowerType() {
        return this.powerType;
    }

    public int getLife() {
        return this.life;
    }

    /**
     * Get player stats
     * @return a data
     */
    public Data getPlayerData() {
        return new Data(this.points, this.speed, this.powerType, this.lastShot, this.life, this.fireRate, this.isInvincible);
    }

    /**
     * Setting new player data
     * @param data new data
     */
    public void setPlayerData(Data data) {
        this.powerType = data.getActivePower();
        this.lastShot = data.getLastShot();
        this.speed = data.getSpeed();
        this.life = data.getLife();
        this.points = data.getPoints();
        this.fireRate = data.getFireRate();
        this.isInvincible = data.getInvincibility();
    }

    /**
     * Adding texture to indicate player (if is P1 or P2)
     */
    private void addPlayerName() {
        Texture texture = new Texture(FXGL.image("indicators/" + this.playerType.name() + ".png"));

        StackPane stackPane = new StackPane(texture);
        stackPane.setTranslateY(-32);
        stackPane.setTranslateX(2);

        this.entity.getViewComponent().addChild(stackPane);
    }

    /**
     * Reseting player data and power
     */
    private void resetPlayer() {
        if ((this.activePower != null) && (System.currentTimeMillis() - this.lastUsePower > 5000)) {
            this.activePower = null;
            this.setPlayerData(new Data(this.points, 200, null, this.lastShot, this.life, 250, false));
            this.entity.getViewComponent().removeChild(this.playerShield);
        }
    }
}


