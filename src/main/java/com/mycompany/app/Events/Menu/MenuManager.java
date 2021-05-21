package com.mycompany.app.Events.Menu;


public class MenuManager implements MenuListener{
    private boolean multiplayer = false;

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }

    @Override
    public void isMultiplayer() {
        multiplayer = true;
    }

    @Override
    public boolean getMultiplayer() {
        return multiplayer;
    }
}
