package com.mycompany.app.Power;

import com.mycompany.app.Characters.PlayerTypes;
import com.mycompany.app.Save.Data;

import java.util.Map;

public class Invincible implements Power{

    private Data data;
    @Override
    public Data use(Data oldData) {
        oldData.setIsInvincible(true);
        return oldData;
    }

    @Override
    public void executionTime() {
    }
}
