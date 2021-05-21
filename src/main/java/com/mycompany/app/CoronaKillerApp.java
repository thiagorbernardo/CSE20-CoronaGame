package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.mycompany.app.Characters.EnemyA;
import com.mycompany.app.Characters.EntityType;
import com.mycompany.app.Characters.PlayerTypes;
import com.mycompany.app.Controller.Game;
import com.mycompany.app.Controller.GameController;
import com.mycompany.app.Controller.GameFactory;
import com.mycompany.app.Projectiles.Bullet;
import com.mycompany.app.Save.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Random;


public class CoronaKillerApp extends GameApplication {
    private final GameFactory gameFactory = new GameFactory();
    double elapsedTime = 0;
    private Entity player, player2;
    private Text textPixels = new Text();
    private double initTime = System.currentTimeMillis();
    private double spawnTimer = 2000;
    private double lastSpawn = 0;

    /* Ranking */
    private RankingDAO rank = new RankingJSON();

    /* Game Controller */
    private Game gameController;

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

        settings.setDeveloperMenuEnabled(true);
        // settings.setMainMenuEnabled(true);

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
            this.gameController.checkDeathCondition(player);
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            this.gameController.playerBulletHittingEnemy(bullet.getComponent(Bullet.class).getBulletOwner());
            this.textPixels.setText("Pontuação: " + String.format("%.0f", this.gameController.getPlayersPoints()));
            bullet.removeFromWorld();
            enemy.removeFromWorld();
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.ENEMYB, (player, enemy) -> {
            this.gameController.checkDeathCondition(player);
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.ENEMYB, (bullet, enemy) -> {
            this.gameController.playerBulletHittingEnemy(bullet.getComponent(Bullet.class).getBulletOwner());
            this.textPixels.setText("Pontuação: " + String.format("%.0f", this.gameController.getPlayersPoints()));
            bullet.removeFromWorld();
            enemy.removeFromWorld();
        });

        /* Collisions BULLET -> BULLET */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.BULLET, (bullet1, bullet2) -> {
            bullet1.removeFromWorld();
            bullet2.removeFromWorld();

            System.out.println("BULLET WITH BULLET");
        });

        /* Collisions SOMETHING -> DOOR */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.DOOR, (bullet, door) -> {
            bullet.removeFromWorld();
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.DOOR, (player, door) -> {
            this.gameController.playerCanLevelUp(door);
        });

        /* Collisions SOMETHING -> WALL */

        FXGL.onCollision(EntityType.ENEMY, EntityType.WALL, (enemy, wall) -> {
            // System.out.println("Enemy hitting wall - change directions?");
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
        });

        /* Collisions SOMETHING -> SCREEN */

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.SCREEN, (player, screen) -> {
            System.out.println("Hitted wall");
            this.gameController.changeCurrentLevel(player.getPosition());
        });
    }

    @Override
    protected void initUI() {
        this.textPixels.setTranslateX(18);
        this.textPixels.setTranslateY(35);

        Font font = new Font(20);
        this.textPixels.setFont(font);

        FXGL.getGameScene().addUINode(this.textPixels); // add to the scene graph
    }

    /**
     * Init configurations of the FXGL game
     */
    @Override
    protected void initGame() {
        this.gameController.initGame();

        this.player = this.gameController.getPlayer(PlayerTypes.P1);
        this.player2 = this.gameController.getPlayer(PlayerTypes.P2);

        this.gameFactory.newEnemy(700, 500, EntityType.ENEMY)
                .getComponent(EnemyA.class).followPlayer(this.player);

        this.gameFactory.newEnemy(900, 300, EntityType.ENEMYB);
    }

    @Override
    protected void onPreInit() {
        super.onPreInit();

        this.gameController = new GameController();

        this.gameController.preInitGame();
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
                    gameFactory.newEnemy(30, 360, EntityType.ENEMY)
                            .getComponent(EnemyA.class).followPlayer(player);
                    break;

                case 1:
                    gameFactory.newEnemy(1200, 360, EntityType.ENEMY)
                            .getComponent(EnemyA.class).followPlayer(player);
                    break;

                case 2:
                    gameFactory.newEnemy(640, 640, EntityType.ENEMY).
                            getComponent(EnemyA.class).followPlayer(player);
                    break;

                case 3:
                    gameFactory.newEnemy(640, 30, EntityType.ENEMY)
                            .getComponent(EnemyA.class).followPlayer(player);
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
}
