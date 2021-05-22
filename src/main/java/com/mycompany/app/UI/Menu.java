package com.mycompany.app.UI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.DialogService;
import com.almasb.fxgl.ui.UIFactoryService;
import com.mycompany.app.Events.Menu.MenuListener;
import com.mycompany.app.Events.Menu.MenuManager;
import com.mycompany.app.Save.*;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import com.mycompany.app.Save.Ranking;
import com.mycompany.app.Save.Save;

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
    private UIFactoryService fxglFactoryService = FXGL.getUIFactoryService();
    private DialogService fxglDialogService = FXGL.getDialogService();

    /**
     * Creates main menu with background and body with buttons.
     * @param type menu type Game or Main.
     */
    public Menu(MenuType type) {
        super(type);
        Texture bg = texture("background/background1.png", 1296, 720);
        bg.setTranslateY(0);
        bg.setTranslateX(0);


        Node body = createBody();
        body.setTranslateY(0);
        getContentRoot().getChildren().addAll(bg, body);
    }

    /**
     * Sets animation when the menu is created.
     */
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

    /**
     * Creates a group of buttons
     * @return the node with buttons
     */
    protected Node createBody() {

        Node btn0 = createActionButton(createStringBinding(() -> "NEW GAME"), this::fireNewGame);
        Node btn1 = createActionButton(createStringBinding(() -> "MULTIPLAYER"), this::startGameMultiplayer);
        Node btn2 = createActionButton(createStringBinding(() -> "LOAD"), this::load);
        Node btn3 = createActionButton(createStringBinding(() -> "RANKING"), this::rank);
        Node btn4 = createActionButton(createStringBinding(() -> "QUIT"), this::fireExit);

        Group group = new Group(btn0, btn1, btn2, btn3, btn4);

        int i = 0;
        for (Node n : group.getChildren()) {

            Point2D vector = new Point2D(100, 150);

            n.setLayoutX(vector.getX());
            n.setLayoutY(vector.getY() + i * 75);
            i++;
        }

        return group;
    }

    /**
     * Creates the buttons with name and actions.
     *
     * @param name   button name (with binding).
     * @param action button action.
     * @return the button.
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

        buttons.add(btn);

        Rectangle clip = new Rectangle(200, 50);
        clip.translateXProperty().bind(btn.translateXProperty().negate());

        btn.setTranslateX(-200);
        btn.setClip(clip);
        btn.setCache(true);
        btn.setCacheHint(CacheHint.SPEED);

        return btn;
    }

    /**
     * Menu listener to enable multiplayer mode.
     */
    private void startGameMultiplayer() {

        this.menuListener.setMultiplayer();

        fireNewGame();
    }

    /**
     * Shows a box with options to select the type of rank to show.
     */
    private void rank() {

        String msg = "Escolha o tipo de Ranking:";

        Button btnRankJSON = this.fxglFactoryService.newButton("Ranking JSON");
        Button btnRankTXT = this.fxglFactoryService.newButton("Ranking TXT");

        btnRankJSON.setAlignment(Pos.CENTER);
        btnRankTXT.setAlignment(Pos.CENTER);

        btnRankJSON.setTranslateX(-10);
        btnRankTXT.setTranslateX(10);

        Rectangle a = new Rectangle(1,1);

        btnRankJSON.setOnAction(e -> {
            this.showRank(new RankingJSON());
        });
        btnRankTXT.setOnAction(e -> {
            this.showRank(new RankingTXT());
        });

        fxglDialogService.showBox(msg, a, btnRankJSON, btnRankTXT);

    }

    /**
     * Shows a box with the top 5 players.
     * @param rank Rank type (JSON or txt).
     */
    private void showRank(RankingDAO rank) {

        int i = 1;
        String msg = "Top 5 Cowboys\n\n";
        for (Ranking ranking : rank.getTopPlayers()) {
            msg += i++ + ". " + ranking.name + ": " + ranking.points + "\n\n";
        }

        fxglDialogService.showMessageBox(msg);
    }

    /**
     * Shows a box with options to select the type of load (JSON or txt).
     */
    private void load() {
        String msg = "Escolha o tipo de Load:";

        Button btnRankJSON = this.fxglFactoryService.newButton("Load JSON");
        Button btnRankTXT = this.fxglFactoryService.newButton("Load TXT");

        btnRankJSON.setAlignment(Pos.CENTER);
        btnRankTXT.setAlignment(Pos.CENTER);

        btnRankJSON.setTranslateX(-10);
        btnRankTXT.setTranslateX(10);

        Rectangle a = new Rectangle(1 ,1);

        btnRankJSON.setOnAction(e -> {
            this.loadSave(new SaveJSON());
        });
        btnRankTXT.setOnAction(e -> {
            this.loadSave(new SaveTXT());
        });

        fxglDialogService.showBox(msg,a , btnRankJSON, btnRankTXT);

    }

    /**
     * Gets the data in the save and sets on the menu listener.
     * @param save type (JSON or txt).
     */
    private void loadSave(SaveDAO save) {

        Save saveFile = save.read();
        if(saveFile != null) {
            this.menuListener.setSave(saveFile);
            fireNewGame();
        } else {
            FXGL.showMessage("Não existe um arquivo de save para este formato!");
        }
    }

    /**
     * Gets the menu listener.
     * @return menu listener.
     */
    public MenuListener getMenuListener() {
        return menuListener;
    }
}

