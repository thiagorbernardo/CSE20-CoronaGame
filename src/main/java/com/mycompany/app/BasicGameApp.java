package com.mycompany.app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

import java.util.List;

enum EntityType {
    PLAYER, BULLET, ENEMY
}

public class BasicGameApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setManualResizeEnabled(true);
        settings.setTitle("TecProg Game");
        settings.setVersion("0.1");
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();

            System.out.println("On Collision");
        });

//        FXGL.onCollisionBegin(EntityType.ENEMY, EntityType.PLAYER, (enemy, player) -> {
//            showMessage("You Died!", () -> {
//                getGameController().startNewGame();
//            });
//        });
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.D, () -> {
//            player.setScaleX(2);
            player.translateX(5); // move right 5 pixels
        });

        FXGL.onKey(KeyCode.A, () -> {
//            player.setScaleX(-2);
            player.translateX(-5); // move left 5 pixels
        });

        FXGL.onKey(KeyCode.W, () -> {
            player.translateY(-5); // move up 5 pixels
        });

        FXGL.onKey(KeyCode.S, () -> {
            player.translateY(5); // move down 5 pixels
        });

        FXGL.onKey(KeyCode.SPACE , () -> {
            this.bullet = this.gameFactory.newBullet(this.player);
        });

        FXGL.onKey(KeyCode.L , () -> {
            this.gameFactory.newEnemey(this.player);
        });
    }

    private GameFactory gameFactory = new GameFactory();
    private Entity player;
    private Entity bullet;

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(gameFactory);
//        FXGL.spawn("player");

        player = this.gameFactory.newPlayer();

//        player = FXGL.entityBuilder()
//                .at(300, 300)
//                .view("stat.png")
//                .scale(2, 2)
//                .buildAndAttach();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

class GameFactory implements EntityFactory {
    @Spawns("player")
    public Entity newPlayer() {

        var channel = new AnimationChannel(List.of(
                FXGL.image("player1/player (1).png"),
                FXGL.image("player1/player (2).png"),
                FXGL.image("player1/player (3).png"),
                FXGL.image("player1/player (4).png")
        ), Duration.seconds(0.5));

        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(300,300)
                .scale(0.5, 0.5)
                .view(new AnimatedTexture(channel).loop())
                .bbox(new HitBox(BoundingShape.box(40, 40)))
//                .view(new Rectangle(40, 40, Color.BLUE))
                .collidable()
                .buildAndAttach();
    }

    @Spawns("bullet")
    public Entity newBullet(Entity player) {
        Point2D direction = FXGL.getInput().getMousePositionWorld()
                .subtract(player.getX() + 300, player.getY() + 130); //aqui pega o centro

        return FXGL.entityBuilder()
                .type(EntityType.BULLET)
                .at(player.getX() + 300, player.getY() + 130)
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
                .at(player.getX() + 300, player.getY() + 300)
                .bbox(new HitBox(BoundingShape.box(100, 100)))
                .view(new Rectangle(100, 100, Color.BLUE))
                .collidable()
                .buildAndAttach();
    }
}