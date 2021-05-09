package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.mycompany.app.Characters.Direction;
import com.mycompany.app.Characters.Enemy;
import com.mycompany.app.Characters.Player;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

enum EntityType {
    PLAYER, BULLET, ENEMY, BACKGROUND, WALL, SCREEN, BOX, DOOR
}

public class CoronaKillerApp extends GameApplication {
    /* Setting game settings, such as screen size */
    @Override
    protected void initSettings(GameSettings settings) {
        // settings.setFullScreenAllowed(true);
        // settings.setFullScreenFromStart(true);

        settings.setWidth(1296);
        settings.setHeight(720);
        settings.setTitle("The Last Cowboy");
        settings.setVersion("1.0");
        settings.setApplicationMode(ApplicationMode.DEVELOPER);

        settings.setDeveloperMenuEnabled(true);
//        settings.setMainMenuEnabled(true);

        settings.setAppIcon("icons/icon.png");
    }

    /**
     * Initing physics manager
     */
    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);

        /* Collisions SOMETHING -> ENEMY */

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.ENEMY, (player, enemy) -> {
            int playerLife = this.player.getComponent(Player.class).damage();
            Sound deathSound = FXGL.getAssetLoader().loadSound("death.wav");

            System.out.println(playerLife);
            System.out.println(this.player.isColliding(this.enemy));

            if (playerLife <= 0) {
                FXGL.getAudioPlayer().playSound(deathSound);
                FXGL.showMessage("Perdeu!", () -> {
                    FXGL.getGameWorld().reset();
                    FXGL.getGameController().startNewGame();
                    FXGL.getAudioPlayer().stopAllSoundsAndMusic();
                    this.textPixels.setText("");
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

        /* Collisions SOMETHING -> DOOR */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.DOOR, (bullet, door) -> {
            bullet.removeFromWorld();
            this.bullet = null;
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.DOOR, (player, door) -> {
            System.out.println("here");
            if(this.player.getComponent(Player.class).getPoints() > 20 && level == 1)
                door.removeFromWorld();
            else if(this.player.getComponent(Player.class).getPoints() > 40 && level == 2)
                door.removeFromWorld();
        });

        /* Collisions SOMETHING -> WALL */

        FXGL.onCollision(EntityType.ENEMY, EntityType.WALL, (enemy, wall) -> {
            // System.out.println("Enemy hitting wall - change directions?");
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
            this.bullet = null;
        });

        /* Collisions SOMETHING -> SCREEN */

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.SCREEN, (player, screen) -> {
            Point2D playerPosition = player.getPosition();
             System.out.println("Next level" + playerPosition);

            if(playerPosition.getX() > 1200) {
                System.out.println("right");
                this.setLevel(new SpawnData(1210, playerPosition.getY())); // RIGHT
            }
            else if(playerPosition.getX() < 100) {
                System.out.println("left");

                this.setLevel(new SpawnData(40, playerPosition.getY())); //LEFT
            }
            else if(playerPosition.getY() > 600) {
                System.out.println("bottom");
                this.setLevel(new SpawnData(playerPosition.getX(), 630)); //BOTTOM
            }
            else if(playerPosition.getY() < 100) {
                System.out.println("top");
                this.setLevel(new SpawnData(playerPosition.getX(), 50)); //TOP
            }
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
            protected void onAction() {
                if (player.getComponent(Player.class).canShot()) {
                    bullet = player.getComponent(Player.class).shotProjectile(gameFactory);
                    FXGL.getAudioPlayer().playSound(shotSound);
                }
            }
        }, KeyCode.SPACE);

        FXGL.getInput().addAction(new UserAction("Enemy") {
            @Override
            protected void onActionBegin() {
                enemy = gameFactory.newEnemy(player, 700, 500);
                enemy.getComponent(Enemy.class).followPlayer(player);
            }
        }, KeyCode.L);

        FXGL.getInput().addAction(new UserAction("DEV") {
            @Override
            protected void onActionBegin() {
                if(FXGL.getDevService().isDevPaneOpen())
                    FXGL.getDevService().closeDevPane();
                else FXGL.getDevService().openDevPane();
            }
        }, KeyCode.F1);
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
    Sound shotSound;
    private Entity player, bullet, enemy, box;
    private int level = 0;
    private Text textPixels = new Text();

    /**
     * Init configurations of the FXGL game
     */
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(gameFactory);
        this.setLevel(new SpawnData());

        this.shotSound = FXGL.getAssetLoader().loadSound("shot.wav");
        Sound levelSound = FXGL.getAssetLoader().loadSound("level1.wav");
        // FXGL.getAudioPlayer().playSound(levelSound);

        this.gameFactory.newWallScreen();
        this.player = this.gameFactory.newPlayer(new SpawnData(300, 300));

        this.enemy = this.gameFactory.newEnemy(this.player, 700, 500);

        this.enemy.getComponent(Enemy.class).followPlayer(this.player);
         FXGL.run(() -> {
         Random gerador = new Random();
         switch (gerador.nextInt(4)){
         case 0:
         enemy = gameFactory.newEnemy(player, 30, 360);
         enemy.getComponent(Enemy.class).followPlayer(player);
         break;

         case 1:
         enemy = gameFactory.newEnemy(player, 1200, 360);
         enemy.getComponent(Enemy.class).followPlayer(player);
         break;

         case 2:
         enemy = gameFactory.newEnemy(player, 640, 640);
         enemy.getComponent(Enemy.class).followPlayer(player);
         break;

         case 3:
         enemy = gameFactory.newEnemy(player, 640, 30);
         enemy.getComponent(Enemy.class).followPlayer(player);
         break;
         }
         }, Duration.seconds(2));
    }

    protected void setLevel(SpawnData spawnLocation){
        System.out.println();
        if (player != null) {
            Data playerData = this.player.getComponent(Player.class).getPlayerData();

            FXGL.setLevelFromMap("level" + ++this.level + ".tmx");
            this.gameFactory.newWallScreen();

            this.player = this.gameFactory.newPlayer(spawnLocation);
            this.player.getComponent(Player.class).setPlayerData(playerData);
//            FXGL.getGameController().gotoGameMenu();
            return;
        }
        FXGL.setLevelFromMap("level" + ++this.level + ".tmx");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
