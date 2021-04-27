package com.mycompany.app;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.image;

public class BulletComponent extends Component {
    private double speed;
    private double xDirection;
    private double yDirection;

    public BulletComponent(Point2D destiny, double speed) {
        this.speed = speed;
        this.xDirection = destiny.getX();
        this.yDirection = destiny.getY();
    }

    @Override
    public void onUpdate(double tpf) {
        if(this.xDirection > 0)
            entity.translateX(tpf * this.speed);
        else if(this.xDirection < 0)
            entity.translateX(-tpf * this.speed);
        if(this.yDirection > 0)
            entity.translateY(tpf * this.speed);
        else if(this.yDirection < 0)
            entity.translateY(-tpf * this.speed);
    }

    public void increaseSpeed(){
        this.speed += 100;
    }

}
