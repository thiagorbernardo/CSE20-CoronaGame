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

public class PlayerComponent extends Component {
    private final int width = 17;
    private final int height = 16;
    private final int speed = 3;

    private Point2D lastPostion;

    private PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animUp, animUpIdle, animDown, animDownIdle, animRight, animRightIdle, animLeft, animLeftIdle;

    private Direction activeDirection = Direction.DOWN;

    public PlayerComponent() {
        Image image = image("player/player.png");
        int qtdImages = 10;

        animDown = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 0, 1);
        animDownIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 8, 8);
        animUp = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 2, 3);
        animUpIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 9, 9);
        animRight = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 4, 5);
        animRightIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 4, 4);
        animLeft = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 6, 7);
        animLeftIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 7, 7);

        texture = new AnimatedTexture(animDownIdle);
        texture.loop();
    }

    @Override
    public void onAdded() {
        entity.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
        entity.getViewComponent().addChild(texture);
    }

    @Override
    public void onUpdate(double tpf) {
        AnimationChannel activeAnimationChannel = this.texture.getAnimationChannel();

        System.out.println(isMoving());

        if(this.activeDirection == Direction.LEFT){
            if(activeAnimationChannel != animLeft) {
                    texture.loopAnimationChannel(animLeft);
            }
        } else if(this.activeDirection == Direction.RIGHT){
            if(activeAnimationChannel != animRight) {
                texture.loopAnimationChannel(animRight);
            }

        } else if(this.activeDirection == Direction.UP){
            if(activeAnimationChannel != animUp) {
                texture.loopAnimationChannel(animUp);
            }

        } else if(this.activeDirection == Direction.DOWN){
            if(activeAnimationChannel != animDown) {
                texture.loopAnimationChannel(animDown);
            }
        }
    }

    private boolean isMoving() {
        return this.lastPostion == getEntity().getPosition();
    }

    private void updatePosition(){
        System.out.println("Movement udpated");
        System.out.println(this.lastPostion);
        System.out.println("Movement udpated");

        this.lastPostion = getEntity().getPosition();
    }

    public void left() {
        this.activeDirection = Direction.LEFT;
        getEntity().translateX(-this.speed);
        this.updatePosition();
    }

    public void right() {
        this.activeDirection = Direction.RIGHT;
        getEntity().translateX(this.speed);
        this.updatePosition();
    }

    public void up() {
        this.activeDirection = Direction.UP;
        getEntity().translateY(-this.speed);
        this.updatePosition();
    }

    public void down() {
        this.activeDirection = Direction.DOWN;
        getEntity().translateY(this.speed);
        this.updatePosition();
    }

    public void stop() {
    }
}
