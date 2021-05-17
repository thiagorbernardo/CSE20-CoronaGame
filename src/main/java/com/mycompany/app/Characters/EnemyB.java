package com.mycompany.app.Characters;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import java.util.Random;
import com.almasb.fxgl.dsl.FXGL;

import java.awt.*;

public class EnemyB extends Character implements Ia {
    private Entity player;
    private double lastMovement = System.currentTimeMillis();

    public EnemyB(String path, int speed, int life) {
        super("enemy/" + path + ".png", 50,48, speed, life);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);

        if(canMove())
        movement();
    }

    @Override
    public void movement() {

         Random gerador = new Random();
         switch (gerador.nextInt(100)/25){
         case 0:
             this.stop();
             this.up();
         break;

         case 1:
             this.stop();
             this.down();
         break;

         case 2:
             this.stop();
             this.left();
         break;

         case 3:
             this.stop();
            this.right();
         break;
         }

        lastMovement = System.currentTimeMillis();

    }

    public boolean canMove() {
        double diffFromLastMove = System.currentTimeMillis() - this.lastMovement;
        return diffFromLastMove > 500;
    }
}