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
        if(this.player != null) {
            System.out.println(this.entity.isColliding(this.player));

            double x = (this.player.getX() - this.entity.getX());
            double y = (this.player.getY() - this.entity.getY());

            if(x < 0){
                this.physics.setVelocityX(-this.speed);
            }else if (x > 0){
                this.physics.setVelocityX(this.speed);
            }else{
                this.physics.setVelocityX(0);
            }
            if(y < 0){
                this.physics.setVelocityY(-this.speed);
            }else if (y > 0){
                this.physics.setVelocityY(this.speed);
            }else{
                this.physics.setVelocityY(0);
            }

        }
    }

    public void followPlayer(Entity charToFollow){

        this.player = charToFollow;

    }
}
