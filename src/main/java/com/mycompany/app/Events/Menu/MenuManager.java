package com.mycompany.app.Events.Menu;


import com.mycompany.app.Save.Save;

public class MenuManager implements MenuListener {
    private Save save;
    private boolean multiplayer = false;

    @Override
    public Save getSave() {
        return this.save;
    }

    @Override
    public void setSave(Save save) {
        this.save = save;
    }

    @Override
    public void setMultiplayer() {
        this.multiplayer = true;
    }

    @Override
    public boolean getMultiplayer() {
        return this.multiplayer;
    }
}
