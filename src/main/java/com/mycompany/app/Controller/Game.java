package com.mycompany.app.Controller;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.mycompany.app.Characters.PlayerTypes;
import javafx.geometry.Point2D;

public interface Game {

    void preInitGame();

    void initGame();

    Entity getPlayer(PlayerTypes player);

    void manageSounds();

    void changeCurrentLevel(Point2D lastPlayerPosition);

    void setLevel(SpawnData spawnLocation);

    int getCurrentLevel();

    void setPlayerActions(PlayerTypes selectedPlayer);

    void setDevActions();

    Double getPlayersPoints();

    boolean playerCanLevelUp();

    void checkDeathCondition(Entity playerDamaged);

    void playerBulletHittingEnemy(PlayerTypes playerThatHitted);

    void spawnEnemy();

    void setPlayerUIInformation();

    void initPlayerUIInfo();
}
