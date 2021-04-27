package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;

import javafx.scene.input.KeyCode;


enum EntityType {
    PLAYER, BULLET, ENEMY, BACKGROUND, WALL, SCREEN, BOX
}

public class CoronaKillerApp extends GameApplication {
    /* Setting game settings, such as screen size */
    @Override
    protected void initSettings(GameSettings settings) {
//        settings.setFullScreenAllowed(true);
//        settings.setFullScreenFromStart(true);

        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Corona Killer");
        settings.setVersion("0.1");
        settings.setApplicationMode(ApplicationMode.DEVELOPER);
    }

    /**
     * Initing physics manager
     */
    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);

        /* Collisions SOMETHING -> PLAYER */

        /* Collisions SOMETHING -> ENEMY */

        FXGL.onCollision(EntityType.PLAYER, EntityType.ENEMY, (player, enemy) -> {
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
            this.bullet = null;
            System.out.println("On Collision");
        });

        /* Collisions SOMETHING -> SCREEN */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.SCREEN, (bullet, screen) -> {
            System.out.println("Bullet hit screen");
            bullet.removeFromWorld();
        });

        /* Collisions SOMETHING -> WALL */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
            //wall.removeFromWorld();
            this.bullet = null;
        });

        FXGL.onCollision(EntityType.PLAYER, EntityType.WALL, (player, wall) -> {
            System.out.println("Collision Player -> Wall " + this.i++);
            //player.getComponent(PlayerComponent.class).setCollision();
        });
    }

    /**
     * Initing listener for input actions
     */
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
                player.getComponent(PlayerComponent.class).stop(); }
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

        FXGL.getInput().addAction(new UserAction("Shot") {
            @Override
            protected void onActionBegin() {
                bullet = player.getComponent(PlayerComponent.class).shotProjectile(gameFactory);
            }
        }, KeyCode.SPACE);

        FXGL.getInput().addAction(new UserAction("Enemy") {
            @Override
            protected void onActionBegin() {
                enemy = gameFactory.newEnemy(player);
                enemy.getComponent(Enemy.class).followPlayer(player);
            }
        }, KeyCode.L);

    }

    private final GameFactory gameFactory = new GameFactory();
    private Entity player, bullet, enemy, box;
    private int i;


    /**
     * Init configurations of the FXGL game
     */
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(gameFactory);
        FXGL.setLevelFromMap("level1.tmx");

        this.gameFactory.newWallScreen();
        this.player = this.gameFactory.newPlayer();
        this.enemy = this.gameFactory.newEnemy(this.player);
        this.box = this.gameFactory.newBox();
        this.enemy.getComponent(Enemy.class).followPlayer(this.player);

    }


    public static void main(String[] args) {
        launch(args);
    }
}

