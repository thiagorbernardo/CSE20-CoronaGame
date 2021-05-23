package com.mycompany.app.Projectiles;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.Texture;
import com.mycompany.app.Characters.PlayerTypes;
import javafx.geometry.Point2D;

import static com.almasb.fxgl.dsl.FXGL.image;

public class Bullet extends Component {
    private double speed;
    private double xDirection;
    private double yDirection;
    private PlayerTypes bulletOwner;
    private Texture texture;

    public Bullet(PlayerTypes bulletOwner, String path, Point2D destiny, double speed) {
        this.bulletOwner = bulletOwner;
        this.speed = speed;
        this.xDirection = destiny.getX();
        this.yDirection = destiny.getY();
        this.texture = new Texture(image("projectiles/" + path + ".png"));
    }

    @Override
    public void onAdded() {
        this.entity.getViewComponent().addChild(this.texture);
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

    /**
     * Getting owner of a bullet
     * @return bullet owner
     */
    public PlayerTypes getBulletOwner() {
        return this.bulletOwner;
    }
}
