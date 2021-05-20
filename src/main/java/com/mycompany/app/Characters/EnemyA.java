package com.mycompany.app.Characters;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;

import java.awt.*;

public class EnemyA extends Character implements Ia {
    private Entity player;
    private Boolean flag;


    public EnemyA(String path, int speed, int life) {
        super("enemy/" + path + ".png", 50,48, speed, life);
        flag = false;

    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if(!flag) {
            if (this.player != null) {
                movement();
            }
        }
        //System.out.println("Enemy " + this.entity.getPosition());
        //System.out.println(physics.getLinearVelocity());
        this.flag = false;
        System.out.println("aro");
    }

    public void setFlag(Entity w){
        flag = true;
    }

    public void followPlayer(Entity charToFollow){
        this.player = charToFollow;
    }

    @Override
    public void movement() {
        Point2D playerCenter = this.player.getCenter();
        Point2D enemyCenter = this.entity.getCenter();
        double x = (playerCenter.getX() - enemyCenter.getX());
        double y = (playerCenter.getY() - enemyCenter.getY());

//        System.out.println("X :" + this.physics.isMovingX() + " Y: " + this.physics.isMovingY());
        if(y < 0){
            this.up();
        }else if (y > 0){
            this.down();
        }else{
            this.physics.setVelocityX(0);
        }
        if(x < 0){
            this.left();
        }else if (x > 0){
            this.right();
        }else {
           this.physics.setVelocityX(0);
        }

    }
}
