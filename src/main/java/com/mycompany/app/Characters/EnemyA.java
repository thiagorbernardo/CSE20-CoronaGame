package com.mycompany.app.Characters;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;

import java.awt.*;

public class EnemyA extends Character implements Ia {
    private Entity player;
    private Entity wall;
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
        wall = w;
        flag = true;
    }

    public void dodge(){
//        BoundingBoxComponent wallBox = wall.getBoundingBoxComponent();
//        BoundingBoxComponent enemyBox = this.entity.getBoundingBoxComponent();
//        System.out.println(wallBox.getMinXWorld());
//        System.out.println(wallBox.getMaxXWorld());
//        if(wallBox.getMaxXWorld() + 1 == enemyBox.getMinXWorld()){
//            Point2D first  = new Point2D(wallBox.getMaxXWorld(), wallBox.getMinYWorld()),
//                    second = new Point2D(wallBox.getMaxXWorld(), wallBox.getMaxYWorld());
//            if (minorDistance(first, second)){
//                this.down();
//            }else{
//                this.up();
//            }
//        }else if(wallBox.getMinXWorld() - 1 == enemyBox.getMaxXWorld()) {
//            Point2D first  = new Point2D(wallBox.getMinXWorld(), wallBox.getMinYWorld()),
//                    second = new Point2D(wallBox.getMinXWorld(), wallBox.getMaxYWorld());
//            if (minorDistance(first, second)) {
//                this.down();
//            } else {
//                this.up();
//            }
//        }else if(wallBox.getMinYWorld() - 1 == enemyBox.getMaxYWorld()) {
//            Point2D first  = new Point2D(wallBox.getMinXWorld(), wallBox.getMinYWorld()),
//                    second = new Point2D(wallBox.getMaxXWorld(), wallBox.getMinYWorld());
//            if (minorDistance(first, second)) {
//                this.right();
//            } else {
//                this.left();
//            }
//        }else if(wallBox.getMaxYWorld() + 1 == enemyBox.getMinYWorld()) {
//            Point2D first  = new Point2D(wallBox.getMinXWorld(), wallBox.getMaxYWorld()),
//                    second = new Point2D(wallBox.getMaxXWorld(), wallBox.getMaxYWorld());
//            if (minorDistance(first, second)) {
//                this.right();
//            } else {
//                this.left();
//            }
//        }
    }

    private boolean minorDistance(Point2D a, Point2D b) {
        double d1 = Math.abs(this.entity.getCenter().distance(a));
        double d2 = Math.abs(this.entity.getCenter().distance(b));

        if (d1 > d2){
            return true;
        }else{
            return false;
        }
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
