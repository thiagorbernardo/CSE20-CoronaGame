package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

public class Enemy extends CharacterComponent {

    public Enemy(String path, int speed) {
        super("player/" + path + ".png", speed);
    }


}
