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

public abstract class CharacterComponent extends Component {
    private final int width = 34;
    private final int height = 32;
    protected int speed;

    protected PhysicsComponent physics;

    private AnimatedTexture texture;

    private AnimationChannel animUp, animUpIdle,
            animDown, animDownIdle,
            animRight, animRightIdle,
            animLeft, animLeftIdle;

    public Direction activeDirection = Direction.RIGHT;

    public CharacterComponent(String path, int speed) {
        Image image = image(path);
        int qtdImages = 10;

        this.speed = speed;

        this.animDown = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 0, 1);
        this.animDownIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 8, 8);
        this.animUp = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 2, 3);
        this.animUpIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 9, 9);
        this.animRight = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 4, 5);
        this.animRightIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 5, 5);
        this.animLeft = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 6, 7);
        this.animLeftIdle = new AnimationChannel(image, qtdImages, this.width, this.height, Duration.seconds(1), 7, 7);

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

    public void setCollision() {
        Direction collisionDirection = this.getMovingDirection();
        System.out.println(collisionDirection);
        int distanceToOverwrite = 30;
        switch (collisionDirection){
            case LEFT:
                this.physics.overwritePosition(this.entity.getPosition().add(distanceToOverwrite, 0));
                break;
            case UPLEFT:
                this.physics.overwritePosition(this.entity.getPosition().add(distanceToOverwrite, distanceToOverwrite));
                break;
            case DOWNLEFT:
                this.physics.overwritePosition(this.entity.getPosition().add(distanceToOverwrite, -distanceToOverwrite));
                break;
            case RIGHT:
                this.physics.overwritePosition(this.entity.getPosition().add(-distanceToOverwrite, 0));
                break;
            case UPRIGHT:
                this.physics.overwritePosition(this.entity.getPosition().add(-distanceToOverwrite, distanceToOverwrite));
                break;
            case DOWNRIGHT:
                this.physics.overwritePosition(this.entity.getPosition().add(-distanceToOverwrite, -distanceToOverwrite));
                break;
            case UP:
                this.physics.overwritePosition(this.entity.getPosition().add(0, distanceToOverwrite));
                break;
            case DOWN:
                this.physics.overwritePosition(this.entity.getPosition().add(0, -distanceToOverwrite));
                break;
            case STOP:
                System.out.println("Collision with stop???");
                break;
            default:
                System.out.println("What???");
        }

        //TODO: Bug, as vezes na hora de atualizar a direção buguei o usuário e ele para de dentro da hitbox
    }

    public Direction getMovingDirection() {
        double xSpeed = this.physics.getVelocityX();
        double ySpeed = this.physics.getVelocityY();

        if(xSpeed < 0 && ySpeed == 0)
            return Direction.LEFT;
        else if(xSpeed < 0 && ySpeed < 0)
            return Direction.UPLEFT;
        else if(xSpeed < 0 && ySpeed > 0)
            return Direction.DOWNLEFT;
        else if(xSpeed > 0 && ySpeed == 0)
            return Direction.RIGHT;
        else if(xSpeed > 0 && ySpeed < 0)
            return Direction.UPRIGHT;
        else if(xSpeed > 0 && ySpeed > 0)
            return Direction.DOWNRIGHT;
        else if(xSpeed == 0 && ySpeed < 0)
            return Direction.UP;
        else if(xSpeed == 0 && ySpeed > 0)
            return Direction.DOWN;
        else
            return Direction.STOP;
    }

}
