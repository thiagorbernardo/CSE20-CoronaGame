package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

public class PlayerComponent extends CharacterComponent {
    public PlayerComponent(String path, int speed) {
        super("player/" + path + ".png", speed);
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

        ProjectileComponent bullet = new ProjectileComponent(direction, 500);


        return gameFactory.newBullet(origin, bullet, "bullet1");
    }
}
