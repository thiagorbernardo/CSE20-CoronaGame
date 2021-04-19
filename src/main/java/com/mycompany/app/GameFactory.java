package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

public class GameFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer() {

        var channel = new AnimationChannel(List.of(
                FXGL.image("player2/player (1).png"),
                FXGL.image("player2/player (2).png")
        ), Duration.seconds(0.5));

        Point2D anchor = new Point2D(300, 300);

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(anchor)
                .scale(3, 3)
//                .view(new AnimatedTexture(channel).loop())
                .bbox(new HitBox(BoundingShape.box(17, 16)))
//                .view(new Rectangle(10, 10, Color.RED))
//                .ancho()
//                .with(physics)
                .collidable()
                .with(new PlayerComponent())
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity newBullet(Entity player) {
        Point2D direction = FXGL.getInput().getMousePositionWorld()
                .subtract(player.getCenter()); //aqui pega o centro

        return FXGL.entityBuilder()
                .type(EntityType.BULLET)
                .at(player.getPosition())
                .viewWithBBox(new Circle(5, 2, 2, Color.BLACK))
                .collidable()
                .with(new ProjectileComponent(direction, 1000))
                .with(new OffscreenCleanComponent())
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity newEnemey(Entity player) {
        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(player.getX() + 100, player.getY() + 100)
                .bbox(new HitBox(BoundingShape.box(100, 100)))
                .view(new Rectangle(100, 100, Color.BLUE))
                .collidable()
                .buildAndAttach();
    }
}