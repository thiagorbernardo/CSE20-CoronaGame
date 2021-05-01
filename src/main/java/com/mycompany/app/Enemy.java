package com.mycompany.app;

import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;

public class Enemy extends Character {
    private Entity player;

    public Enemy(String path, int speed, int life) {
        super("enemy/" + path + ".png", 50,48, speed, life);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if(this.player != null) {
            this.chasingMovement();
        }
    }

    private void chasingMovement() {
        Point2D playerCenter = this.player.getCenter();
        Point2D enemyCenter = this.entity.getCenter();
        double x = (playerCenter.getX() - enemyCenter.getX());
        double y = (playerCenter.getY() - enemyCenter.getY());

//        System.out.println("X :" + this.physics.isMovingX() + " Y: " + this.physics.isMovingY());

        if(x < 0){
            this.left();
        }else if (x > 0){
            this.right();
        }else{
            this.physics.setVelocityX(0);
//                this.setIdleAnimation();
        }
        if(y < 0){
            this.up();
        }else if (y > 0){
            this.down();
        }else{
            this.physics.setVelocityY(0);
//                this.setIdleAnimation();
        }
    }

    public void followPlayer(Entity charToFollow){
        this.player = charToFollow;
    }
}
