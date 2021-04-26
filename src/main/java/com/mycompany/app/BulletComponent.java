package com.mycompany.app;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ProjectileWithAccelerationComponent;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

public class BulletComponent extends Component {
//    private Point2D acceleration;

    private Point2D direction;

//    private PhysicsComponent physics;

    private double speed;

    private Point2D velocity;

    private ProjectileWithAccelerationComponent projectile;

//    private AnimatedTexture texture;

//    private AnimationChannel animation;

    public BulletComponent(Point2D direction, double speed) {
//        this.acceleration = acceleration;
        this.direction = direction;
        this.speed = speed;
//
//        this.texture = new AnimatedTexture(animRightIdle);
//        this.texture.loop();
    }

//    @Override
//    public void onAdded() {
//        entity.getViewComponent().addChild(texture);
//    }

    @Override
    public void onUpdate(double tpf) {
//        this.physics.setVelocityX(this.speed);
//        this.physics.setVelocityX(this.direction.getX());
//        this.physics.setVelocityY(this.direction.getY());

        this.projectile = new ProjectileWithAccelerationComponent(this.direction, 500);

        if (this.entity.getX() <= 0) {
            this.entity.removeFromWorld();
        }

        if (this.entity.getRightX() >= FXGL.getAppWidth()) {
            this.entity.removeFromWorld();
        }
    }

}
