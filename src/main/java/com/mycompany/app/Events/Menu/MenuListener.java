package com.mycompany.app.Events.Menu;

import com.mycompany.app.Save.Save;

public interface MenuListener {
    void setSave(Save save);

    Save getSave();

    public void setMultiplayer();

    boolean getMultiplayer();
}
