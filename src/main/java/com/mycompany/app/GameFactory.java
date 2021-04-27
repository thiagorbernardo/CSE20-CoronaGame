package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GameFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer() {

        Point2D anchor = new Point2D(300, 300);

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        Rectangle rect = new Rectangle(34, 32, Color.BLUE);

        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(anchor)
                .scale(2, 2)
                .viewWithBBox(rect)
                .with(physics)
                .collidable()
                .with(new PlayerComponent("player", 300, 1))
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity newBullet(Point2D origin, ProjectileComponent projectile, String path) {
        return FXGL.entityBuilder()
                .type(EntityType.BULLET)
                .viewWithBBox(FXGL.texture("projectiles/" + path + ".png"))
                .at(origin)
                .collidable()
                .with(projectile)
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity newEnemy(Entity player) {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);

        Rectangle rect = new Rectangle(34, 32, Color.BLUE);

        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(700, 500)
                .scale(1.5, 1.5)
                .viewWithBBox(rect)
                .with(physics)
                .collidable()
                .with(new Enemy("player2", 100, 1))
                .buildAndAttach();
    }

    /**
     * Method to create a entity wall
     * @param data tmx tile data
     * @return a Entity
     */
    @Spawns("wall")
    public Entity newWall(SpawnData data) {

        return FXGL.entityBuilder(data)
                .type(EntityType.WALL)
                .at(data.getX(), data.getY())
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
//                .view(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.RED))
                .with(new PhysicsComponent())
                .collidable()
                .build();
    }

    @Spawns("screen")
    public Entity newWallScreen() {
        return FXGL.entityBuilder()
                .type(EntityType.SCREEN)
                .collidable()
                .buildScreenBoundsAndAttach(10);
    }

    //TODO: Only for tests
    @Spawns("box")
    public Entity newBox() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.STATIC);

        Rectangle rect = new Rectangle(34, 32, Color.BLUE);

        return FXGL.entityBuilder()
                .type(EntityType.BOX)
                .at(400, 400)
                .viewWithBBox(rect)
                .with(physics)
                .collidable()
                .buildAndAttach();


    }
}