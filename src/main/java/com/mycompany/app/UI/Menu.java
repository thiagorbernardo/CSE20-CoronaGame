package com.mycompany.app.UI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.mycompany.app.Events.Menu.MenuListener;
import com.mycompany.app.Events.Menu.MenuManager;
import com.mycompany.app.Save.Ranking;
import com.mycompany.app.Save.RankingDAO;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import com.mycompany.app.Save.Ranking;
import com.mycompany.app.Save.RankingJSON;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.beans.binding.Bindings.createStringBinding;
import static javafx.beans.binding.Bindings.when;

public class Menu extends FXGLMenu {


    private List<Node> buttons = new ArrayList<>();
    private int animIndex = 0;
    private boolean flag = true;
    private MenuListener menuListener = new MenuManager();

    public Menu(MenuType type) {
        super(type);
        Texture bg = texture("background/background1.png", 1296, 720);
        bg.setTranslateY(0);
        bg.setTranslateX(0);


        Node body = createBody();
        body.setTranslateY(0);
        getContentRoot().getChildren().addAll(bg, body);
    }

    @Override
    public void onCreate() {
        animIndex = 0;

        buttons.forEach(btn -> {
            btn.setOpacity(0);

            animationBuilder(this)
                    .delay(Duration.seconds(animIndex * 0.1))
                    .interpolator(Interpolators.BACK.EASE_OUT())
                    .translate(btn)
                    .from(new Point2D(-200, 0))
                    .to(new Point2D(0, 0))
                    .buildAndPlay();

            animationBuilder(this)
                    .delay(Duration.seconds(animIndex * 0.1))
                    .fadeIn(btn)
                    .buildAndPlay();

            animIndex++;
        });
    }

    protected Node createBody() {

        Node btn0 = createActionButton(createStringBinding(() -> "NEW GAME"), this::fireNewGame);
        Node btn1 = createActionButton(createStringBinding(() -> "MULTIPLAYER"), this::StartGame);
        Node btn2 = createActionButton(createStringBinding(() -> "LOAD"), this::fireResume);
        Node btn3 = createActionButton(createStringBinding(() -> "RANKING"), this::Rank);
        Node btn4 = createActionButton(createStringBinding(() -> "QUIT"), this::fireExit);

        Group group = new Group(btn0, btn1, btn2, btn3, btn4);

        int i = 0;
        for (Node n : group.getChildren()) {

            Point2D vector = new Point2D(100, 150);
//                    .normalize()

            n.setLayoutX(vector.getX());
            n.setLayoutY(vector.getY() + i * 75);
            i++;
        }

        return group;
    }

    private void StartGame() {

        this.menuListener.setMultiplayer();

        fireNewGame();
    }

    private void Rank() {


        System.out.println("teste");
    }

    /**
     * Creates a new button with given name that performs given action on click/press.
     *
     * @param name   button name (with binding)
     * @param action button action
     * @return new button
     */
    protected Node createActionButton(StringBinding name, Runnable action) {
        Rectangle bg = new Rectangle(200, 50);
        bg.setEffect(new BoxBlur());

        Text text = getUIFactoryService().newText(name);
        text.setTranslateX(15);
        text.setFill(Color.web("#f28526"));
        StackPane btn = new StackPane(bg, text);

        bg.fillProperty().bind(when(btn.hoverProperty())
                .then(Color.BLACK)
                .otherwise(Color.TRANSPARENT)
        );

        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setOnMouseClicked(e -> action.run());

        // clipping
        buttons.add(btn);

        Rectangle clip = new Rectangle(200, 50);
        clip.translateXProperty().bind(btn.translateXProperty().negate());

        btn.setTranslateX(-200);
        btn.setClip(clip);
        btn.setCache(true);
        btn.setCacheHint(CacheHint.SPEED);

        return btn;
    }

    public MenuListener getMenuListener() {
        return menuListener;
    }
}

