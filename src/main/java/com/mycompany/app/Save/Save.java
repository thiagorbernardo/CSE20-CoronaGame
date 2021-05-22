package com.mycompany.app.Save;

import com.almasb.fxgl.entity.Entity;
import com.mycompany.app.Characters.Player;
import com.mycompany.app.Characters.PlayerTypes;

import java.util.Map;

public class Save {
    public long createdAt = System.currentTimeMillis();
    public boolean isMultiplayer;
    public int level;
    public Data P1Data;
    public Data P2Data;

    public Save(boolean isMultiplayer, int level, Map<PlayerTypes, Entity> players) {
        this.isMultiplayer = isMultiplayer;
        this.level = level;
        if(players != null)
            this.mapsToObjects(players);
    }

    private void mapsToObjects(Map<PlayerTypes, Entity> players){
        this.P1Data = players.get(PlayerTypes.P1).getComponent(Player.class).getPlayerData();

        System.out.println(this.isMultiplayer);
        if(this.isMultiplayer)
            this.P2Data = players.get(PlayerTypes.P2).getComponent(Player.class).getPlayerData();
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setMultiplayer(boolean multiplayer) {
        isMultiplayer = multiplayer;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setP1Data(Data p1Data) {
        P1Data = p1Data;
    }

    public void setP2Data(Data p2Data) {
        P2Data = p2Data;
    }
}
