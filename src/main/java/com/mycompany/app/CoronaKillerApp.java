package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.mycompany.app.Characters.Character;
import com.mycompany.app.Characters.Direction;
import com.mycompany.app.Characters.EnemyA;
import com.mycompany.app.Characters.EnemyB;
import com.mycompany.app.Characters.Player;
import com.mycompany.app.Save.Ranking;
import com.mycompany.app.Save.RankingJSON;
import com.mycompany.app.UI.Menu;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

enum EntityType {
    PLAYER, BULLET, ENEMY, BACKGROUND, WALL, SCREEN, BOX, DOOR, ENEMYB
}

public class CoronaKillerApp extends GameApplication {
    private final GameFactory gameFactory = new GameFactory();
    Sound shotSound;
    double elapsedTime = 0;
    private Entity player, bullet, enemy, enemyb;
    private int level = 0;
    private Text textPixels = new Text();
    private double initTime = System.currentTimeMillis();
    private double spawnTimer = 2000;
    private double lastSpawn = 0;
    private RankingJSON rank = new RankingJSON();

    public static void main(String[] args) {
        launch(args);
    }

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
//         settings.setSceneFactory(new SceneFactory() {
// //            @Override
// //            public FXGLMenu newGameMenu() {
// //                return new Menu();
// //            }
//             @Override
//             public FXGLMenu newMainMenu() {
//                 return new Menu();
//             }
//         });
//        settings.setDeveloperMenuEnabled(true);

        settings.setMainMenuEnabled(true);
//        settings.setSceneFactory(new SceneFactory());
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
                rank.save(new Ranking("joao", this.player.getComponent(Player.class).getPoints()));

                for (Ranking ranking : rank.getTopPlayers()) {
                    System.out.println(ranking.name + ": " + ranking.points);
                }
                FXGL.showMessage("Perdeu!", () -> {
                    FXGL.getGameWorld().reset();
                    FXGL.getGameController().startNewGame();
                    FXGL.getAudioPlayer().stopAllSoundsAndMusic();
                    this.textPixels.setText("");
                });
            }
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.ENEMYB, (player, enemy) -> {
            int playerLife = this.player.getComponent(Player.class).damage();
            Sound deathSound = FXGL.getAssetLoader().loadSound("death.wav");

            System.out.println(playerLife);
            System.out.println(this.player.isColliding(this.enemyb));

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
            this.player.getComponent(Player.class).hit();
            double points = this.player.getComponent(Player.class).getPoints();
            System.out.println(points);
            this.textPixels.setText("Pontuação: " + points);
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            this.bullet = null;
            System.out.println("Hitting enemy");
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMYB, (bullet, enemyb) -> {
            this.player.getComponent(Player.class).hit();
            double points = this.player.getComponent(Player.class).getPoints();
            System.out.println(points);
            this.textPixels.setText("Pontuação: " + String.format("%.0f", points));
            bullet.removeFromWorld();
            enemyb.removeFromWorld();
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
            if (this.player.getComponent(Player.class).getPoints() > 20 && level == 1)
                door.removeFromWorld();
            else if (this.player.getComponent(Player.class).getPoints() > 40 && level == 2)
                door.removeFromWorld();
        });

        /* Collisions SOMETHING -> WALL */

        FXGL.onCollision(EntityType.ENEMY, EntityType.WALL, (enemy, wall) -> {
            System.out.println("Enemy hitting wall - change directions?");
            enemy.getComponent(EnemyA.class).setFlag(wall);
            System.out.println();
        });


        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
            this.bullet = null;
        });

        /* Collisions SOMETHING -> SCREEN */

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.SCREEN, (player, screen) -> {
            Point2D playerPosition = player.getPosition();
            System.out.println("Next level" + playerPosition);

            if (playerPosition.getX() > 1200) {
                System.out.println("right");
                this.setLevel(new SpawnData(1210, playerPosition.getY())); // RIGHT
            } else if (playerPosition.getX() < 100) {
                System.out.println("left");

                this.setLevel(new SpawnData(40, playerPosition.getY())); // LEFT
            } else if (playerPosition.getY() > 600) {
                System.out.println("bottom");
                this.setLevel(new SpawnData(playerPosition.getX(), 630)); // BOTTOM
            } else if (playerPosition.getY() < 100) {
                System.out.println("top");
                this.setLevel(new SpawnData(playerPosition.getX(), 50)); // TOP
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

        FXGL.getInput().addAction(new UserAction("DEV") {
            @Override
            protected void onActionBegin() {
                if (FXGL.getDevService().isDevPaneOpen())
                    FXGL.getDevService().closeDevPane();
                else
                    FXGL.getDevService().openDevPane();
            }
        }, KeyCode.F1);
    }

    @Override
    protected void initUI() {
        this.textPixels.setTranslateX(30);
        this.textPixels.setTranslateY(30);

        Font font = new Font(20);
        this.textPixels.setFont(font);

        FXGL.getGameScene().addUINode(this.textPixels); // add to the scene graph
    }

    /**
     * Init configurations of the FXGL game
     */
    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(gameFactory);
        this.setLevel(new SpawnData());

        this.shotSound = FXGL.getAssetLoader().loadSound("shot.wav");
        Sound levelSound = FXGL.getAssetLoader().loadSound("starwars.mp3");
         FXGL.getAudioPlayer().playSound(levelSound);

        this.gameFactory.newWallScreen();
        this.player = this.gameFactory.newPlayer(new SpawnData(300, 300));

        this.enemy = this.gameFactory.newEnemy(this.player, 700, 500, EntityType.ENEMY);

        this.enemy.getComponent(EnemyA.class).followPlayer(this.player);
        this.enemyb = this.gameFactory.newEnemy(this.player, 900, 300, EntityType.ENEMYB);
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);
        Random gerador = new Random();
        String enemysprite = "enemy1";

        if ((System.currentTimeMillis() - lastSpawn) > spawnTimer) {

            int rnd = gerador.nextInt(3);
            if(rnd == 0)
                enemysprite = "enemy1";
            if(rnd == 1)
                enemysprite = "enemy2";
            if(rnd == 2)
                enemysprite = "enemy3";

            switch (gerador.nextInt(4)) {
                case 0:
                    enemy = gameFactory.newEnemy(player, 30, 360, EntityType.ENEMY);
                    enemy.getComponent(EnemyA.class).followPlayer(player);
                    break;

                case 1:
                    enemy = gameFactory.newEnemy(player, 1200, 360, EntityType.ENEMY);
                    enemy.getComponent(EnemyA.class).followPlayer(player);
                    break;

                case 2:
                    enemy = gameFactory.newEnemy(player, 640, 640, EntityType.ENEMY);
                    enemy.getComponent(EnemyA.class).followPlayer(player);
                    break;

                case 3:
                    enemy = gameFactory.newEnemy(player, 640, 30, EntityType.ENEMY);
                    enemy.getComponent(EnemyA.class).followPlayer(player);
                    break;
            }
            this.lastSpawn = System.currentTimeMillis();
        }

        elapsedTime = elapsedTime + 10;

        if (elapsedTime > 2000 && spawnTimer > 500) {
            // Para tornar o jogo mais difícil, pode-se alterar o passo em que diminui-se
            // o spawn timer
            spawnTimer = spawnTimer - 50;
            elapsedTime = 0;
        }

        System.out.println(spawnTimer);

    }

    protected void setLevel(SpawnData spawnLocation) {
        System.out.println();
        if (player != null) {
            Data playerData = this.player.getComponent(Player.class).getPlayerData();

            FXGL.setLevelFromMap("level" + ++this.level + ".tmx");
            this.gameFactory.newWallScreen();

            this.player = this.gameFactory.newPlayer(spawnLocation);
            this.player.getComponent(Player.class).setPlayerData(playerData);
            // FXGL.getGameController().gotoGameMenu();
            return;
        }
        FXGL.setLevelFromMap("level" + ++this.level + ".tmx");
    }
}
