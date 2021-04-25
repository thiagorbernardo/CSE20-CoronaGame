package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

public class GameFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer() {

        Point2D anchor = new Point2D(300, 300);

        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(anchor)
                .scale(2, 2)
                .bbox(new HitBox(BoundingShape.box(34, 32)))
                .view(new Rectangle(34, 32, Color.RED))
                .with(physics)
                .collidable()
                .with(new PlayerComponent("player"))
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity newBullet(Entity player) {
        Point2D playerCenter = player.getCenter();

//        player.getComponent(PlayerComponent.class).activeDirection

        Point2D direction = playerCenter.subtract(-FXGL.getAppWidth(), playerCenter.getY());

        ProjectileComponent bullet = new ProjectileComponent(direction, 500);
        bullet.allowRotation(false);

        return FXGL.entityBuilder()
                .type(EntityType.BULLET)
                .viewWithBBox(FXGL.texture("projectiles/bullet1.png"))
                .at(player.getCenter())
                .collidable()
                .with(bullet)
                .with(new OffscreenCleanComponent())
                .buildAndAttach();
    }

    @Spawns("enemy")
    public Entity newEnemy(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();
//        physics.setBodyType(BodyType.STATIC);

        System.out.println(data + "POSITION------------" + data.getX() + ", " + data.getY());
        System.out.println(data + "WIDTH-----------------" + data.<Integer>get("width") + ", " + data.<Integer>get("height"));
        return FXGL.entityBuilder(data)
                .type(EntityType.ENEMY)
                .at(data.getX(), data.getY())
//                .scale(2, 2)
//                .at(player.getX() + 100, player.getY() + 100)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width"), data.<Integer>get("height"))))
                .view(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.RED))
//                .bbox(new HitBox(BoundingShape.box(34, 32)))
                .with(physics)
                .collidable()
//                .with(new PlayerComponent("player2"))
                .build();
    }
    @Spawns("background")
    public Entity newBackground() {
        double w = FXGL.getSettings().getWidth();
        double h = FXGL.getSettings().getHeight();
        double thickness = -30;
        return FXGL.entityBuilder()
                .type(EntityType.BACKGROUND)
                .view(FXGL.texture("1.jpg"))
                .bbox(new HitBox("LEFT",  new Point2D(-thickness, 0), BoundingShape.box(thickness, h)))
                .bbox(new HitBox("RIGHT", new Point2D(w, 0), BoundingShape.box(thickness, h)))
                .bbox(new HitBox("TOP",   new Point2D(0, -thickness), BoundingShape.box(w, thickness)))
                .bbox(new HitBox("BOT",   new Point2D(0, h), BoundingShape.box(w, thickness)))
                .collidable()
                .with(new PhysicsComponent())
                .buildAndAttach();
    }
}