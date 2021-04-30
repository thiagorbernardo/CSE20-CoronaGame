package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

public class Player extends Character {

    private int points = 0;

    public Player(String path, int speed, int life) {
        super("player/" + path + ".png", 34, 32, speed, life);
    }

    public Entity shotProjectile(GameFactory gameFactory) {
        Point2D origin = this.entity.getCenter();

        Point2D direction;

        switch (this.activeDirection){
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

        return gameFactory.newBullet(origin, direction, "bullet1");
    }

    public double hit(){
        return this.points += 10;
    }
}
