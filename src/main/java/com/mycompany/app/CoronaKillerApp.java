package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.mycompany.app.Characters.*;
import com.mycompany.app.Controller.Game;
import com.mycompany.app.Controller.GameController;
import com.mycompany.app.Controller.GameFactory;
import com.mycompany.app.Events.Sound.MusicsNames;
import com.mycompany.app.Projectiles.Bullet;
import com.mycompany.app.Save.*;
import com.mycompany.app.UI.GameMenu;
import com.mycompany.app.UI.Menu;
import com.mycompany.app.UI.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Random;
import java.util.function.Predicate;


public class CoronaKillerApp extends GameApplication {
    private Text textPixels = new Text("Pontuação: 0");
    private Scene scene = new Scene();

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


        settings.setSceneFactory(scene);

        settings.setDeveloperMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setAppIcon("icons/icon.png");
    }

    /**
     * Initing physics manager
     */
    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().setGravity(0, 0);

        /* Collisions SOMETHING -> ENEMY */

        FXGL.onCollisionBegin(EntityType.PLAYER, EnemyType.ENEMYA, (player, enemy) -> {
            this.gameController.checkDeathCondition(player);
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EnemyType.ENEMYC, (player, enemy) -> {
            this.gameController.checkDeathCondition(player);
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EnemyType.ENEMYB, (player, enemy) -> {
            this.gameController.checkDeathCondition(player);
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EnemyType.ENEMYA, (bullet, enemy) -> {
            this.gameController.playerBulletHittingEnemy(bullet.getComponent(Bullet.class).getBulletOwner());
            this.textPixels.setText("Pontuação: " + String.format("%.0f", this.gameController.getPlayersPoints()));
            bullet.removeFromWorld();
            enemy.removeFromWorld();
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EnemyType.ENEMYB, (bullet, enemy) -> {
            this.gameController.playerBulletHittingEnemy(bullet.getComponent(Bullet.class).getBulletOwner());
            this.textPixels.setText("Pontuação: " + String.format("%.0f", this.gameController.getPlayersPoints()));
            bullet.removeFromWorld();
            enemy.removeFromWorld();
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EnemyType.ENEMYC, (bullet, enemy) -> {
            this.gameController.playerBulletHittingEnemy(bullet.getComponent(Bullet.class).getBulletOwner());
            this.textPixels.setText("Pontuação: " + String.format("%.0f", this.gameController.getPlayersPoints()));
            bullet.removeFromWorld();
            enemy.removeFromWorld();
        });

        FXGL.onCollision(EnemyType.ENEMYA, EnemyType.ENEMYC, (enemyA, enemyC) -> {
            enemyA.getComponent(EnemyA.class).setFlag();
            enemyC.getComponent(EnemyA.class).setFlag();
        });

        /* Collisions BULLET -> BULLET */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.BULLET, (bullet1, bullet2) -> {
            bullet1.removeFromWorld();
            bullet2.removeFromWorld();
        });

        /* Collisions SOMETHING -> DOOR */

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.DOOR, (bullet, door) -> {
            bullet.removeFromWorld();
        });

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.DOOR, (player, door) -> {
            this.gameController.playerCanLevelUp();
            door.removeFromWorld();
        });

        /* Collisions SOMETHING -> WALL */

        FXGL.onCollision(EnemyType.ENEMYA, EntityType.WALL, (enemy, wall) -> {
            enemy.getComponent(EnemyA.class).setFlag();
        });

        FXGL.onCollision(EnemyType.ENEMYC, EntityType.WALL, (enemy, wall) -> {
            enemy.getComponent(EnemyA.class).setFlag();
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
        });

        /* Collisions SOMETHING -> SCREEN */

        FXGL.onCollisionBegin(EntityType.PLAYER, EntityType.SCREEN, (player, screen) -> {
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
    }

    @Override
    protected void onPreInit() {
        super.onPreInit();

        this.gameController = new GameController(this.scene);

        this.gameController.preInitGame();
    }

    @Override
    protected void onUpdate(double tpf) {
        super.onUpdate(tpf);

        this.gameController.spawnEnemy();
    }
}
