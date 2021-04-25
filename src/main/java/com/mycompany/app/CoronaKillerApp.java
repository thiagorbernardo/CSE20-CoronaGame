package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.virtual.VirtualButton;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.image;


enum EntityType {
    PLAYER, BULLET, ENEMY, BACKGROUND
}

public class CoronaKillerApp extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setManualResizeEnabled(true);
        settings.setTitle("Corona Killer");
        settings.setVersion("0.1");
        settings.setApplicationMode(ApplicationMode.DEVELOPER);

    }

    @Override
    protected void initPhysics() {
        FXGL.onCollision(EntityType.ENEMY, EntityType.PLAYER, (enemy, player) -> {
            System.out.println("Deaddddddddddd");
            Sound deathSound = FXGL.getAssetLoader().loadSound("death.wav");

            FXGL.getAudioPlayer().playSound(deathSound);
            FXGL.showMessage("Perdeu!", () -> {
                FXGL.getGameController().startNewGame();
            });
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            this.enemy = null;
            System.out.println("On Collision");
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.BACKGROUND, (bullet, background) -> {
            bullet.removeFromWorld();
            this.bullet = null;
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.BACKGROUND, (player, background) -> {
            System.out.println("Start of collision between background and player");
//            System.out.println(FXGL.getGameWorld().getEntityByID("LEFT", 1));
            player.getComponent(PlayerComponent.class).stopMovement();
            System.out.println(player.isColliding(background));
        });
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Left") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).down();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(PlayerComponent.class).up();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stop();
            }
        }, KeyCode.W);


        FXGL.onKey(KeyCode.SPACE , () -> {
            this.player.getComponent(PlayerComponent.class).shotProjectile(this.gameFactory);
//            if(this.bullet == null)
//                this.bullet = this.gameFactory.newBullet(this.player);
        });

        FXGL.onKey(KeyCode.L , () -> {
//            if(this.enemy == null)
//                System.out.println("fix");
//                this.enemy = this.gameFactory.newEnemy();
                FXGL.spawn("enemy");
        });
    }

    private GameFactory gameFactory = new GameFactory();
    private Entity player, bullet, enemy, background;

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(gameFactory);
        FXGL.setLevelFromMap("level1.tmx");
//        FXGL.getGameWorld().setLevel(level);

//        this.background = this.gameFactory.newBackground();
//        System.out.println(this.background.getBoundingBoxComponent().getHeight());
        player = this.gameFactory.newPlayer();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

