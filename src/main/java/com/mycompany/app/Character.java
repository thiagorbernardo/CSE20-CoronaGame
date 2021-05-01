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
    RIGHT, LEFT, UP, DOWN,
    UPRIGHT, UPLEFT,
    DOWNRIGHT, DOWNLEFT,
    STOP
}

public abstract class Character extends Component {
    private int width;
    private int height;
    protected int speed;

    protected int life;

    protected PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animUp, animUpIdle,
            animDown, animDownIdle,
            animRight, animRightIdle,
            animLeft, animLeftIdle;

    protected Direction activeDirection = Direction.RIGHT;

    public Character(String path, int width, int height, int speed, int life) {
        Image image = image(path);
        int qtdImages = 10;
        Duration animationTime = Duration.seconds(0.5);

        this.width = width;
        this.height = height;
        this.speed = speed;
        this.life = life;


        this.animDown = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 0, 1);
        this.animDownIdle = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 8, 8);
        this.animUp = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 2, 3);
        this.animUpIdle = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 9, 9);
        this.animRight = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 4, 5);
        this.animRightIdle = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 5, 5);
        this.animLeft = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 6, 7);
        this.animLeftIdle = new AnimationChannel(image, qtdImages, this.width, this.height, animationTime, 7, 7);

        this.texture = new AnimatedTexture(this.animRightIdle);
        this.texture.loop();
    }

    @Override
    public void onAdded() {
        this.entity.getViewComponent().addChild(this.texture);
    }

    @Override
    public void onUpdate(double tpf) {
        setTexture();
        if(!this.physics.isMoving()){
            this.setIdleAnimation();
        }
    }

    protected void setTexture() {
        AnimationChannel activeAnimationChannel = this.texture.getAnimationChannel();
        if (this.physics.isMoving()) {
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
        this.physics.setVelocityY(this.speed);
    }

    /**
     * Method to stop movement from player
     */
    public void stop() {
        this.physics.setVelocityX(0);
        this.physics.setVelocityY(0);
    }

    /**
     * Character suffer damage
     */
    public int damage(){
      return this.life--;
    }

    protected void setIdleAnimation() {
        if (this.activeDirection == Direction.LEFT) {
            texture.loopAnimationChannel(animLeftIdle);
        } else if (this.activeDirection == Direction.RIGHT) {
            texture.loopAnimationChannel(animRightIdle);

        } else if (this.activeDirection == Direction.UP) {
            texture.loopAnimationChannel(animUpIdle);

        } else if (this.activeDirection == Direction.DOWN) {
            texture.loopAnimationChannel(animDownIdle);
        }
    }

}
