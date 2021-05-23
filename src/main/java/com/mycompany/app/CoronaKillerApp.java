package com.mycompany.app;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;

import com.mycompany.app.Characters.*;
import com.mycompany.app.Controller.Game;
import com.mycompany.app.Controller.GameController;
import com.mycompany.app.Projectiles.Bullet;
import com.mycompany.app.UI.Scene;


public class CoronaKillerApp extends GameApplication {
    private Scene scene = new Scene();

    /* Game Controller */
    private Game gameController;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Setting game settings, such as screen size
     * @param settings GameSettings that come from FXGL
     */
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
            bullet.removeFromWorld();
            enemy.removeFromWorld();
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EnemyType.ENEMYB, (bullet, enemy) -> {
            this.gameController.playerBulletHittingEnemy(bullet.getComponent(Bullet.class).getBulletOwner());
            bullet.removeFromWorld();
            enemy.removeFromWorld();
        });

        FXGL.onCollisionBegin(EntityType.BULLET, EnemyType.ENEMYC, (bullet, enemy) -> {
            this.gameController.playerBulletHittingEnemy(bullet.getComponent(Bullet.class).getBulletOwner());
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
            if(this.gameController.playerCanLevelUp()){
            door.removeFromWorld();
            }

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

    /**
     * Initialize UI objects.
     */
    @Override
    protected void initUI() {
        this.gameController.initPlayerUIInfo();
    }

    /**
     * Init configurations of the FXGL game
     */
    @Override
    protected void initGame() {
        this.gameController.initGame();
    }

    /**
     * Called once per application lifetime, just before initGame().
     */
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
