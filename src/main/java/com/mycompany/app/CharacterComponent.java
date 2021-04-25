package com.mycompany.app;

import com.almasb.fxgl.entity.component.Component;
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

    private boolean canMove = true;

    private PhysicsComponent physics;

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

        if(this.physics.isMoving()) {
            if (this.activeDirection == Direction.LEFT) {
                if (activeAnimationChannel != animLeft) {
                    texture.loopAnimationChannel(animLeft);
                }
            } else if (this.activeDirection == Direction.RIGHT) {
                if (activeAnimationChannel != animRight) {
                    texture.loopAnimationChannel(animRight);
                }

            } else if (this.activeDirection == Direction.UP) {
                if (activeAnimationChannel != animUp) {
                    texture.loopAnimationChannel(animUp);
                }

            } else if (this.activeDirection == Direction.DOWN) {
                if (activeAnimationChannel != animDown) {
                    texture.loopAnimationChannel(animDown);
                }
            }
        }
    }

    public void stopMovement(){
//        this.canMove = false;
        this.stop();


//        if(this.activeDirection == Direction.LEFT){
//            this.right();
//        } else if(this.activeDirection == Direction.RIGHT){
//            this.left();
//
//        } else if(this.activeDirection == Direction.UP){
//            this.down();
//
//        } else if(this.activeDirection == Direction.DOWN){
//            this.up();
//        }
    }

    public void left() {
        if(this.canMove) {
            this.activeDirection = Direction.LEFT;
            this.physics.setVelocityX(-this.speed);
        }
    }

    public void right() {
        if(this.canMove) {
            this.activeDirection = Direction.RIGHT;
            this.physics.setVelocityX(this.speed);
        }

    }

    public void up() {
        if(this.canMove) {
            this.activeDirection = Direction.UP;
            this.physics.setVelocityY(-this.speed);
        }

    }

    public void down() {
        if(this.canMove) {
            this.activeDirection = Direction.DOWN;
            this.physics.setVelocityY(300);
        }

    }

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
}
