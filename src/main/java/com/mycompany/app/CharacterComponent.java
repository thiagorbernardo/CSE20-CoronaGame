package com.mycompany.app;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.image;

enum Direction {
    RIGHT, LEFT, UP, DOWN
}

public abstract class CharacterComponent extends Component {
    private final int width = 34;
    private final int height = 32;
    private final int speed = 300;

    private PhysicsComponent physics;

    private Entity isCollidingWith = null;

    private AnimatedTexture texture;

    private AnimationChannel animUp, animUpIdle, animDown, animDownIdle, animRight, animRightIdle, animLeft, animLeftIdle;

    public Direction activeDirection = Direction.RIGHT;

    public CharacterComponent(String path) {
        Image image = image(path);
        int qtdImages = 10;

        animDown = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 0, 1);
        animDownIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 8, 8);
        animUp = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 2, 3);
        animUpIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 9, 9);
        animRight = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 4, 5);
        animRightIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 5, 5);
        animLeft = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 6, 7);
        animLeftIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 7, 7);

        texture = new AnimatedTexture(animRightIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        AnimationChannel activeAnimationChannel = this.texture.getAnimationChannel();
//        this.canMove = true;
//
        if(this.physics.isMoving()) {
            switch (this.activeDirection) {
                case LEFT:
                    if (activeAnimationChannel != animLeft)
                        texture.loopAnimationChannel(animLeft);
                    break;
                case RIGHT:
                    if (activeAnimationChannel != animRight)
                        texture.loopAnimationChannel(animRight);
                    break;
                case UP:
                    if (activeAnimationChannel != animUp)
                        texture.loopAnimationChannel(animUp);
                    break;
                case DOWN:
                    if (activeAnimationChannel != animDown)
                        texture.loopAnimationChannel(animDown);
                    break;
            }
        }
    }

    /**
     * Method to move player leftwards
     */
    public void left() {
            this.activeDirection = Direction.LEFT;
            this.physics.setVelocityX(-this.speed);
    }

    /**
     * Method to move player rightwards
     */
    public void right() {
            this.activeDirection = Direction.RIGHT;
            this.physics.setVelocityX(this.speed);
    }

    /**
     * Method to move player upwards
     */
    public void up() {
            this.activeDirection = Direction.UP;
            this.physics.setVelocityY(-this.speed);
    }

    /**
     * Method to move player downwards
     */
    public void down() {
            this.activeDirection = Direction.DOWN;
            this.physics.setVelocityY(300);
    }

    /**
     * Method to stop movement from player
     */
    public void stop() {
        this.physics.setVelocityX(0);
        this.physics.setVelocityY(0);

        if(this.activeDirection == Direction.LEFT){
            texture.loopAnimationChannel(animLeftIdle);
        } else if(this.activeDirection == Direction.RIGHT){
            texture.loopAnimationChannel(animRightIdle);

        } else if(this.activeDirection == Direction.UP){
            texture.loopAnimationChannel(animUpIdle);

        } else if(this.activeDirection == Direction.DOWN){
            texture.loopAnimationChannel(animDownIdle);
        }
    }

    public void setCollision(Entity object) {
            if(this.entity.isColliding(object)) {
                this.stop();
                switch (this.activeDirection) {
                    case LEFT:
                        this.physics.overwritePosition(this.entity.getPosition().add(5, 0));
                        this.activeDirection = Direction.RIGHT;
//                        this.isCollidingWith = null;
                        break;
                    case RIGHT:
                        this.physics.overwritePosition(this.entity.getPosition().add(-5, 0));
                        this.activeDirection = Direction.LEFT;
//                        this.isCollidingWith = null;
                        break;
                    case UP:
                        this.physics.overwritePosition(this.entity.getPosition().add(0, 5));
                        this.activeDirection = Direction.DOWN;
//                        this.isCollidingWith = null;
                        break;
                    case DOWN:
                        this.physics.overwritePosition(this.entity.getPosition().add(0, -5));
                        this.activeDirection = Direction.UP;
//                        this.isCollidingWith = null;
                        break;
                }
            }
        }
}
