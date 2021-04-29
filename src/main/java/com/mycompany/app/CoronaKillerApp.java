package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.Random;
import javafx.util.Duration;

enum EntityType {
    PLAYER, BULLET, ENEMY, BACKGROUND, WALL, SCREEN, BOX
}

public class CoronaKillerApp extends GameApplication {
    /* Setting game settings, such as screen size */
    @Override
    protected void initSettings(GameSettings settings) {
        // settings.setFullScreenAllowed(true);
        // settings.setFullScreenFromStart(true);

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

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.ENEMY, (player, enemy) -> {
            int playerLife = this.player.getComponent(Player.class).damage();
            Sound deathSound = FXGL.getAssetLoader().loadSound("death.wav");

            System.out.println(playerLife);
            System.out.println(this.player.isColliding(this.enemy));

            if(playerLife <= 0) {
                FXGL.getAudioPlayer().playSound(deathSound);
                FXGL.showMessage("Perdeu!", () -> {
                    FXGL.getGameController().startNewGame();
                });
            }
        });


        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            double points = this.player.getComponent(Player.class).hit();
            System.out.println(points);
            this.textPixels.setText("Pontuação: " + points);
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            this.bullet = null;
            System.out.println("Hitting enemy");
        });

        /* Collisions SOMETHING -> SCREEN */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.SCREEN, (bullet, screen) -> {
            bullet.removeFromWorld();
        });

        /* Collisions SOMETHING -> WALL */

        FXGL.onCollision(EntityType.ENEMY, EntityType.WALL, (enemy, wall) -> {
//            System.out.println("Enemy hitting wall - change directions?");
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
            //wall.removeFromWorld();
            this.bullet = null;
        });

        FXGL.onCollision(EntityType.PLAYER, EntityType.WALL, (player, wall) -> {
//            System.out.println("Collision Player -> Wall " + this.i++);
//            player.getComponent(PlayerComponent.class).setCollision();
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
                player.getComponent(Player.class).left();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(Player.class).stop();
            }
        }, KeyCode.A);

        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onAction() {
                player.getComponent(Player.class).right();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(Player.class).stop();
            }
        }, KeyCode.D);

        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onAction() {
                player.getComponent(Player.class).down();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(Player.class).stop();
            }
        }, KeyCode.S);

        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onAction() {
                player.getComponent(Player.class).up();
            }

            @Override
            protected void onActionEnd() {
                player.getComponent(Player.class).stop();
            }
        }, KeyCode.W);

        FXGL.getInput().addAction(new UserAction("Shot") {
            @Override
            protected void onActionBegin() {
                bullet = player.getComponent(Player.class).shotProjectile(gameFactory);
            }
        }, KeyCode.SPACE);

        FXGL.getInput().addAction(new UserAction("Enemy") {
            @Override
            protected void onActionBegin() {
                enemy = gameFactory.newEnemy(player, 700, 500);
                enemy.getComponent(Enemy.class).followPlayer(player);
            }
        }, KeyCode.L);
    }

    @Override
    protected void initUI() {
        this.textPixels.setTranslateX(75);
        this.textPixels.setTranslateY(75);

        Font font = new Font(20);
        this.textPixels.setFont(font);

        FXGL.getGameScene().addUINode(this.textPixels); // add to the scene graph
    }

    private final GameFactory gameFactory = new GameFactory();
    private Entity player, bullet, enemy, box;
    private int i;
    private Text textPixels = new Text();

    /**
     * Init configurations of the FXGL game
     */
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(gameFactory);
        FXGL.setLevelFromMap("level1.tmx");

        this.gameFactory.newWallScreen();
        this.player = this.gameFactory.newPlayer();
        this.enemy = this.gameFactory.newEnemy(this.player, 700, 500);
        // this.box = this.gameFactory.newBox();
        this.enemy.getComponent(Enemy.class).followPlayer(this.player);
        Random gerador = new Random();
        FXGL.run(() -> {
            
               switch (gerador.nextInt(4)){
                   case 0:
                       enemy = gameFactory.newEnemy(player, 700, 500);
                       enemy.getComponent(Enemy.class).followPlayer(player);
                       break;

                   case 1:
                        enemy = gameFactory.newEnemy(player, 200, 500);
                        enemy.getComponent(Enemy.class).followPlayer(player);
                        break;

                   case 2:
                       enemy = gameFactory.newEnemy(player, 200, 200);
                       enemy.getComponent(Enemy.class).followPlayer(player);
                       break;

                   case 3:
                       enemy = gameFactory.newEnemy(player, 700, 200);
                       enemy.getComponent(Enemy.class).followPlayer(player);
                       break;
               }

            System.out.println("tempo");

        }, Duration.seconds(2));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
