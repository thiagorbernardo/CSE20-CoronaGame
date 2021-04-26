package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

public class Enemy extends CharacterComponent {
    Entity player;

    public Enemy(String path, int speed) {
        super("player/" + path + ".png", speed);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
//        if(this.player != null)
//            System.out.println(this.entity.isColliding(this.player));
    }

    public void followPlayer(Entity charToFollow){
        this.player = charToFollow;
    }
}
