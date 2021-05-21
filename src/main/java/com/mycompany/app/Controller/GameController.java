package com.mycompany.app.Controller;


import com.almasb.fxgl.app.ReadOnlyGameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;

import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.UIFactoryService;
import com.mycompany.app.Characters.Player;
import com.mycompany.app.Characters.PlayerTypes;

import com.mycompany.app.Save.*;
import com.mycompany.app.Events.Menu.MenuListener;
import com.mycompany.app.Events.Menu.MenuManager;

import com.mycompany.app.Events.Sound.MusicsNames;
import com.mycompany.app.Events.Sound.SoundListener;
import com.mycompany.app.Events.Sound.SoundManager;
import com.mycompany.app.Events.Sound.SoundNames;

import javafx.collections.FXCollections;
import com.mycompany.app.Save.Ranking;
import com.mycompany.app.Save.RankingDAO;
import com.mycompany.app.Save.RankingJSON;

import com.mycompany.app.UI.Scene;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.*;

public class GameController implements Game {
    /* FXGL variables */
    private GameWorld fxglWorld = FXGL.getGameWorld();
    private ReadOnlyGameSettings fxglSettings = FXGL.getSettings();
    private Input input = FXGL.getInput();
    private com.almasb.fxgl.app.GameController fxglGameController = FXGL.getGameController();
    private UIFactoryService fxglFactoryService = FXGL.getUIFactoryService();
    private Scene scene;

    /* Ranking */
    private RankingDAO rank;

    /* Listeners */
    private SoundListener soundListener = new SoundManager();
    private GameFactory gameFactory = new GameFactory();

    /* Maps */
    private Map<PlayerTypes, Entity> players = new HashMap<>();
    private Map<PlayerTypes, Data> playersData = new HashMap<>();
    private Map<PlayerTypes, List<KeyCode>> playersKeyCodes = new HashMap<>();

    /* World variables */
    private int currentLevel = 0;
    private Boolean isMultiplayer = false;
    private Random random = new Random();


    public GameController(Scene scene){
        this.scene = scene;
    }
    /**
     * Manage pre init config, such as sounds and input keys
     */
    @Override
    public void preInitGame() {
        this.manageSounds();
        this.setDevActions();

        this.playersKeyCodes.put(PlayerTypes.P1, Arrays.asList(KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE));
        this.setPlayerActions(PlayerTypes.P1);

        this.playersKeyCodes.put(PlayerTypes.P2, Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.DOWN, KeyCode.ENTER));

    }

    /**
     * Init default config
     */
    @Override
    public void initGame() {

        this.fxglWorld.addEntityFactory(this.gameFactory);

        this.isMultiplayer = this.scene.getMainMenu().getMenuListener().getMultiplayer();

        this.setLevel(new SpawnData());

        this.playRandomMusic();

        this.gameFactory.newWallScreen();

        this.players.put(PlayerTypes.P1, this.gameFactory.newPlayer(PlayerTypes.P1, new SpawnData(300, 300)));

        if (isMultiplayer) {
            this.setPlayerActions(PlayerTypes.P2);
            this.players.put(PlayerTypes.P2, this.gameFactory.newPlayer(PlayerTypes.P2, new SpawnData(300, 300)));
        }

    }

    /**
     * Get a Player from map of players
     *
     * @param player PlayerType (P1, P2)
     * @return A Entity
     */
    @Override
    public Entity getPlayer(PlayerTypes player) {
        return this.players.get(player);
    }

    /**
     * Load all sounds to be available in runtime
     */
    @Override
    public void manageSounds() {
        this.soundListener.loadSounds(
                Arrays.asList(SoundNames.values())
        );

        this.soundListener.loadMusics(
                Arrays.asList(MusicsNames.values())
        );

        this.fxglSettings.setGlobalMusicVolume(0.1);
        this.fxglSettings.setGlobalSoundVolume(0.2);
    }


    /**
     * Change current level
     *
     * @param lastPlayerPosition Point2D to represent the last player position
     */
    @Override
    public void changeCurrentLevel(Point2D lastPlayerPosition) {
        if (lastPlayerPosition.getX() > 1200) { // RIGHT
            this.setLevel(new SpawnData(40, lastPlayerPosition.getY())); // LEFT
        } else if (lastPlayerPosition.getX() < 100) { // LEFT
            this.setLevel(new SpawnData(1210, lastPlayerPosition.getY())); // RIGHT
        } else if (lastPlayerPosition.getY() > 600) { // BOTTOM
            this.setLevel(new SpawnData(lastPlayerPosition.getX(), 50)); // TOP
        } else if (lastPlayerPosition.getY() < 100) { // TOP
            this.setLevel(new SpawnData(lastPlayerPosition.getX(), 630)); // BOTTOM
        }
    }

    /**
     * Get the current level
     *
     * @return a int to represent current level
     */
    @Override
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Set level and spawn player in a specific location
     *
     * @param spawnLocation SpawnData to a specific point in world
     */
    @Override
    public void setLevel(SpawnData spawnLocation) {
        this.soundListener.stopAll();

        Entity p1 = this.players.get(PlayerTypes.P1);
//        System.out.println(p1);
        Entity p2;

        if (p1 != null) {
            Data p1Data = p1.getComponent(Player.class).getPlayerData();
            this.playersData.put(PlayerTypes.P1, p1Data);

            Data p2Data = null;

            if (isMultiplayer) {
                p2 = this.players.get(PlayerTypes.P2);
                p2Data = p2.getComponent(Player.class).getPlayerData();
                this.playersData.put(PlayerTypes.P2, p2Data);
            }

            FXGL.setLevelFromMap("level" + ++this.currentLevel + ".tmx");
            this.gameFactory.newWallScreen();

            this.setEntitiesLocation(spawnLocation);
            this.playRandomMusic();

            return;
        }
        FXGL.setLevelFromMap("level" + ++this.currentLevel + ".tmx");
    }

    /**
     * Setting Player Actions
     *
     * @param selectedPlayer PlayerType to set input actions from a player
     */
    @Override
    public void setPlayerActions(PlayerTypes selectedPlayer) {
        List<KeyCode> keyCodes = this.playersKeyCodes.get(selectedPlayer);

        this.input.addAction(new UserAction(selectedPlayer.name() + "Left") {
            @Override
            protected void onAction() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).left();
            }

            @Override
            protected void onActionEnd() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).stop();
            }
        }, keyCodes.get(0));

        this.input.addAction(new UserAction(selectedPlayer.name() + "Right") {
            @Override
            protected void onAction() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).right();
            }

            @Override
            protected void onActionEnd() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).stop();
            }
        }, keyCodes.get(1));

        this.input.addAction(new UserAction(selectedPlayer.name() + "Up") {
            @Override
            protected void onAction() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).up();
            }

            @Override
            protected void onActionEnd() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).stop();
            }
        }, keyCodes.get(2));

        this.input.addAction(new UserAction(selectedPlayer.name() + "Down") {
            @Override
            protected void onAction() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).down();
            }

            @Override
            protected void onActionEnd() {
                Entity player = players.get(selectedPlayer);
                if (player != null)
                    player.getComponent(Player.class).stop();
            }
        }, keyCodes.get(3));

        this.input.addAction(new UserAction(selectedPlayer.name() + "Shot") {
            @Override
            protected void onAction() {
                Entity player = players.get(selectedPlayer);
                if (player != null && player.getComponent(Player.class).canShot()) {
                    player.getComponent(Player.class).shotProjectile(gameFactory);
                    soundListener.playSound(SoundNames.SHOT);
                }
            }
        }, keyCodes.get(4));
    }

    /**
     * Setting Dev Actions
     */
    @Override
    public void setDevActions() {
        this.input.addAction(new UserAction("DevPanel") {
            @Override
            protected void onActionBegin() {
                if (!FXGL.getDevService().isDevPaneOpen())
                    FXGL.getDevService().openDevPane();
                else
                    FXGL.getDevService().closeDevPane();
            }
        }, KeyCode.F1);

        this.input.addAction(new UserAction("PlayRandomMusic") {
            @Override
            protected void onActionBegin() {
                playRandomMusic();
            }
        }, KeyCode.DIGIT0);
    }

    @Override
    public Double getPlayersPoints() {
        double totalPoints = this.players.get(PlayerTypes.P1).getComponent(Player.class).getPoints();

        if (isMultiplayer)
            totalPoints += this.players.get(PlayerTypes.P2).getComponent(Player.class).getPoints();

        return totalPoints;
    }


    @Override
    public void playerCanLevelUp(Entity door) {
        double totalGamePoints = this.getPlayersPoints();

        System.out.println("Pontuação atual: " + totalGamePoints);

        if (totalGamePoints > 20 && this.currentLevel == 1)
            door.removeFromWorld();
        else if (totalGamePoints > 20 && this.currentLevel == 2)
            door.removeFromWorld();
    }

    @Override
    public void checkDeathCondition(Entity playerDamaged) {

        int playerLife = playerDamaged.getComponent(Player.class).damage();

        if (playerLife <= 0) {
            this.soundListener.playSound(SoundNames.DEATH);
//            for (Ranking ranking : rank.getTopPlayers()) {
//                System.out.println(ranking.name + ": " + ranking.points);
//            }
            String msg = isMultiplayer ? "Vocês conquistaram " : "Você conquistou ";
            msg += String.format("%.0f", this.getPlayersPoints()) + " pontos. Parabéns!\n\n\n      Escolha o método de save do jogo.";

            Button btnRankJSON = this.fxglFactoryService.newButton("Ranking JSON");
            Button btnRankTXT = this.fxglFactoryService.newButton("Ranking TXT");
            TextField textField = new TextField();
            btnRankJSON.setAlignment(Pos.CENTER);
            btnRankTXT.setAlignment(Pos.CENTER);
            textField.setAlignment(Pos.CENTER);
            textField.setMaxWidth(200);

            btnRankJSON.setOnAction(e -> {
                this.rank = new RankingJSON();
                this.rank.save(new Ranking(textField.getText(), this.getPlayersPoints()));
                this.resetGame();
            });
            btnRankTXT.setOnAction(e -> {
                this.rank = new RankingTXT();
                this.rank.save(new Ranking(textField.getText(), this.getPlayersPoints()));
                this.resetGame();
            });

            FXGL.getDialogService().showBox(msg, textField, btnRankJSON, btnRankTXT);

        }
    }

    private void setEntitiesLocation(SpawnData spawnLocation) {

        Data p1Data = this.playersData.get(PlayerTypes.P1);
        Entity p1 = this.gameFactory.newPlayer(PlayerTypes.P1, spawnLocation);
        p1.getComponent(Player.class).setPlayerData(p1Data);
        this.players.put(PlayerTypes.P1, p1);

        if (isMultiplayer) {
            Data p2Data = this.playersData.get(PlayerTypes.P2);
            Entity p2 = this.gameFactory.newPlayer(PlayerTypes.P2, spawnLocation);
            p2.getComponent(Player.class).setPlayerData(p2Data);
            this.players.put(PlayerTypes.P2, p2);
        }

//            FXGL.getGameController().gotoGameMenu();
    }

    private void resetGame() {
        this.soundListener.stopAll();
        this.soundListener.playMusic(MusicsNames.CHAMPIONS);

        this.players.put(PlayerTypes.P1, null);
        if (isMultiplayer)
            this.players.put(PlayerTypes.P2, null);

        this.currentLevel = 0;
        this.fxglWorld.reset();
        this.fxglGameController.startNewGame();
    }

    @Override
    public void playerBulletHittingEnemy(PlayerTypes playerThatHitted) {
        Entity player = this.players.get(playerThatHitted);

        player.getComponent(Player.class).hit();
        double points = player.getComponent(Player.class).getPoints();

        System.out.println(playerThatHitted + "" + points);
    }

    @Override
    public Map<PlayerTypes, Data> getPlayerData() {
        return playersData;
    }

    private void updateUiInformation() {
//        this.textPixels.setText("Pontuação: " + String.format("%.0f", points));
    }

    private void playRandomMusic() {
        this.soundListener.stopAllMusics();
        MusicsNames[] musicsNames = MusicsNames.values();
        MusicsNames someRandomMusic = musicsNames[this.random.nextInt(musicsNames.length)];
        System.out.println(someRandomMusic);

        this.soundListener.playMusic(someRandomMusic);
    }
}
