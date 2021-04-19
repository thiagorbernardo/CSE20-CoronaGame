package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
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


enum EntityType {
    PLAYER, BULLET, ENEMY
}

public class CoronaKillerApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setManualResizeEnabled(true);
        settings.setTitle("Corona Killer");
        settings.setVersion("0.1");
//        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    @Override
    protected void initPhysics() {
        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();

            System.out.println("On Collision");
        });

        FXGL.onCollisionBegin(EntityType.ENEMY, EntityType.PLAYER, (enemy, player) -> {
            System.out.println("Deaddddddddddd");

            FXGL.showMessage("You Died!", () -> {
                FXGL.getGameController().startNewGame();
            });
        });
    }

    @Override
    protected void initInput() {


        FXGL.onKey(KeyCode.D, () -> {
            this.player.getComponent(PlayerComponent.class).right();
        });

        FXGL.onKey(KeyCode.A, () -> {
            this.player.getComponent(PlayerComponent.class).left();
        });

        FXGL.onKey(KeyCode.W, () -> {
            this.player.getComponent(PlayerComponent.class).up();
        });

        FXGL.onKey(KeyCode.S, () -> {
            this.player.getComponent(PlayerComponent.class).down();
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

        player = this.gameFactory.newPlayer();
        player.getBoundingBoxComponent();

        System.out.println(player.getX());
        System.out.println(player.getY());


    }


    public static void main(String[] args) {
        launch(args);
    }
}

