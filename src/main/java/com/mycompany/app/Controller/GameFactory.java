package com.mycompany.app.Controller;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;

import com.mycompany.app.Characters.EntityType;
import javafx.geometry.Point2D;

import com.mycompany.app.Characters.Enemy;
import com.mycompany.app.Characters.Player;
import com.mycompany.app.Characters.PlayerTypes;
import com.mycompany.app.Events.Notification.Notificator;
import com.mycompany.app.Projectiles.Bullet;

public class GameFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer(PlayerTypes playerType, SpawnData data) {

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .scale(1.5, 1.5)
                .bbox(new HitBox(BoundingShape.polygon(9, 9, 9, 32, 26, 32, 26, 9)))
                .with(physics)
                .collidable()
                .with(new Player(playerType, 200, 0, new Notificator()))
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity newBullet(PlayerTypes bulletOwner, Point2D origin, Point2D destiny, String path, double speed) {
        return FXGL.entityBuilder()
                .type(EntityType.BULLET)
                .bbox(new HitBox(BoundingShape.polygon(2, 6, 2, 11, 6, 15, 11, 15, 15, 11, 15, 6, 11, 2, 6, 2)))
                .at(origin)
                .collidable()
                .with(new Bullet(bulletOwner, path, destiny, speed))
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity newEnemy(Entity player, int x, int y) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(x, y) // 700 500
                .scale(1.0, 1.0)
                .bbox(new HitBox(BoundingShape.polygon(17, 6, 17, 42, 34, 42, 34, 6)))
                .with(physics)
                .collidable()
                .with(new Enemy("enemy1", 100, 2))
                .buildAndAttach();
    }

    /**
     * Method to create a entity wall
     *
     * @param data tmx tile data
     * @return a Entity
     */
    @Spawns("wall")
    public Entity newWall(SpawnData data) {

        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    /**
     * Method to create a entity door
     *
     * @param data tmx tile data
     * @return a Entity
     */
    @Spawns("door")
    public Entity newDoor(SpawnData data) {

        PhysicsComponent physics = new PhysicsComponent();

        physics.setBodyType(BodyType.STATIC);

        return FXGL.entityBuilder(data)
                .type(EntityType.DOOR)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .with(physics)
                .collidable()
                .build();
    }

    @Spawns("screen")
    public Entity newWallScreen() {
        return FXGL.entityBuilder()
                .type(EntityType.SCREEN)
                .collidable()
                .buildScreenBoundsAndAttach(1000);
    }
}