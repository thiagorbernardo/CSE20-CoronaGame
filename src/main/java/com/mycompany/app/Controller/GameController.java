package com.mycompany.app.Controller;


import com.almasb.fxgl.app.ReadOnlyGameSettings;
import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;

import com.almasb.fxgl.ui.DialogService;
import com.almasb.fxgl.ui.UIFactoryService;
import com.mycompany.app.Characters.EnemyType;
import com.mycompany.app.Characters.Player;
import com.mycompany.app.Characters.PlayerTypes;

import com.mycompany.app.Power.PowerType;
import com.mycompany.app.Save.*;

import com.mycompany.app.Events.Sound.MusicsNames;
import com.mycompany.app.Events.Sound.SoundListener;
import com.mycompany.app.Events.Sound.SoundManager;
import com.mycompany.app.Events.Sound.SoundNames;

import com.mycompany.app.Save.Ranking;
import com.mycompany.app.Save.RankingDAO;
import com.mycompany.app.Save.RankingJSON;

import com.mycompany.app.UI.Scene;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

public class GameController implements Game {
    /* FXGL variables */
    private GameWorld fxglWorld = FXGL.getGameWorld();
    private ReadOnlyGameSettings fxglSettings = FXGL.getSettings();
    private Input input = FXGL.getInput();
    private com.almasb.fxgl.app.GameController fxglGameController = FXGL.getGameController();
    private UIFactoryService fxglFactoryService = FXGL.getUIFactoryService();
    private GameScene fxglGameScene = FXGL.getGameScene();
    private DialogService fxglDialogService = FXGL.getDialogService();

    /* Ranking */
    private RankingDAO rank;

    /* Save */
    private SaveDAO save;

    /* Listeners */
    private SoundListener soundListener = new SoundManager();
    private GameFactory gameFactory = new GameFactory();

    /* Maps */
    private Map<PlayerTypes, Entity> players = new HashMap<>();
    private Map<PlayerTypes, Data> playersData = new HashMap<>();
    private Map<PlayerTypes, List<KeyCode>> playersKeyCodes = new HashMap<>();

    /* World variables */
    private int mapWidth;
    private int mapHeight;

    private Scene scene;
    private int currentLevel = 0;
    private Boolean isMultiplayer = false;
    private Random random = new Random();

    /* Spawn variables */
    private double spawnTimer = 2000;
    private double lastSpawn = System.currentTimeMillis();
    private double elapsedTime = 0;
    private double enterLevelTime = 0;

    /* UI variables */
    private Text playersPointsUI = new Text("Pontuação: 0");
    private Text p1ActivePowerUI = new Text("");
    private Text p2ActivePowerUI = new Text("");
    private Text playersLifeUI = new Text("Vidas P1: ");


    public GameController(Scene scene) {
        this.scene = scene;
    }

    /**
     * Manage pre init config, such as sounds and input keys
     */
    @Override
    public void preInitGame() {

        this.mapWidth = this.fxglSettings.getWidth();
        this.mapHeight = this.fxglSettings.getHeight();

        this.manageSounds();
        this.setDevActions();

        this.playersKeyCodes.put(PlayerTypes.P1, Arrays.asList(KeyCode.A, KeyCode.D, KeyCode.W, KeyCode.S, KeyCode.SPACE));
        this.setPlayerActions(PlayerTypes.P1);

        this.playersKeyCodes.put(PlayerTypes.P2, Arrays.asList(KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP, KeyCode.DOWN, KeyCode.ENTER));

    }

    /**
     * Init default config of the game,
     * Songs, Musics, Factory, Save, Entities and Players
     */
    @Override
    public void initGame() {
        this.fxglWorld.addEntityFactory(this.gameFactory);

//        this.soundListener.stopAll();

        this.isMultiplayer = this.scene.getMainMenu().getMenuListener().getMultiplayer();

        Save saveFile = this.scene.getMainMenu().getMenuListener().getSave();

        if (saveFile != null) {
            this.isMultiplayer = saveFile.isMultiplayer;
            this.currentLevel = saveFile.level - 1;
        }

        this.setLevel(new SpawnData());

        this.playRandomMusic();

        this.gameFactory.newWallScreen();

        this.manageSave(saveFile);

        this.setPlayerUIInformation();
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
     * Check and spawn players in the desired location, save or default
     * @param saveFile save config
     */
    private void manageSave(Save saveFile) {
        SpawnData standardSpawnLocation = new SpawnData(mapWidth / 2, mapHeight / 2);
        if (saveFile != null) {
            this.playersData.put(PlayerTypes.P1, saveFile.P1Data);

            if (saveFile.P2Data != null)
                this.playersData.put(PlayerTypes.P2, saveFile.P2Data);
            this.setEntitiesLocation(standardSpawnLocation);
        } else {
            // Starting game without load
            this.players.put(PlayerTypes.P1, this.gameFactory.newPlayer(PlayerTypes.P1, standardSpawnLocation));

            if (isMultiplayer)
                this.players.put(PlayerTypes.P2, this.gameFactory.newPlayer(PlayerTypes.P2, standardSpawnLocation));

        }
        if (this.input.getAllBindings().size() <= 7) {
            this.setPlayerActions(PlayerTypes.P2);
        }
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
//        this.soundListener.stopAll();

        Entity p1 = this.players.get(PlayerTypes.P1);
        Entity p2;

        if (p1 != null) {
            this.resetSpawn();
            this.initPlayerOptionToSave();
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
                    List<Point2D> point2DS = player.getComponent(Player.class).shotProjectile();

                    gameFactory.newBullet(selectedPlayer, point2DS.get(0), point2DS.get(1), "bullet1", 400);
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

                save = new SaveTXT();

                Save save1 = new Save(
                        isMultiplayer,
                        currentLevel,
                        players
                );

                save.save(save1);

            }
        }, KeyCode.F1);

        this.input.addAction(new UserAction("PlayRandomMusic") {
            @Override
            protected void onActionBegin() {
                playRandomMusic();

                SaveDAO saveDAO = new SaveJSON();
                saveDAO.save(new Save(
                        isMultiplayer,
                        currentLevel,
                        players
                ));

                System.out.println("Saving game");
            }
        }, KeyCode.DIGIT0);
    }

    /**
     * Method to get all players points
     *
     * @return total points from players
     */
    @Override
    public Double getPlayersPoints() {
        double totalPoints = this.players.get(PlayerTypes.P1).getComponent(Player.class).getPoints();

        if (isMultiplayer)
            totalPoints += this.players.get(PlayerTypes.P2).getComponent(Player.class).getPoints();

        return totalPoints;
    }


    /**
     * Condition to know if a player can level up
     */
    @Override
    public boolean playerCanLevelUp() {
        double totalGamePoints = this.getPlayersPoints();

        return (((this.currentLevel == 1 && totalGamePoints >= 500)
                || (this.currentLevel == 2 && totalGamePoints >= 1000)));
    }

    @Override
    public void checkDeathCondition(Entity playerDamaged) {

        int playerLife = playerDamaged.getComponent(Player.class).damage();

        this.setPlayerUIInformation();

        if (playerLife <= 0) {
            this.soundListener.playSound(SoundNames.DEATH);

            this.soundListener.stopAllMusics();
            this.soundListener.playMusic(MusicsNames.CHAMPIONS);
//            for (Ranking ranking : rank.getTopPlayers()) {
//                System.out.println(ranking.name + ": " + ranking.points);
//            }
            String msg = isMultiplayer ? "  Vocês conquistaram " : "  Você conquistou ";
            msg += String.format("%.0f", this.getPlayersPoints()) + " pontos. Parabéns!\n\n\n      Escolha o método de save do jogo:";

            Button btnRankJSON = this.fxglFactoryService.newButton("Ranking JSON");
            Button btnRankTXT = this.fxglFactoryService.newButton("Ranking TXT");

            btnRankJSON.setAlignment(Pos.CENTER);
            btnRankJSON.setDisable(true);

            btnRankTXT.setAlignment(Pos.CENTER);
            btnRankTXT.setDisable(true);

            TextField textField = new TextField();
            textField.setPromptText("Nome do Player");

            textField.setOnKeyTyped(e -> {
                int charLimit = 12;
                //TODO: Pedir no mínimo 3 letras pra salvar no ranking
                if (textField.getText().length() > 2) {
                    btnRankJSON.setDisable(false);
                    btnRankTXT.setDisable(false);
                } else {
                    btnRankJSON.setDisable(true);
                    btnRankTXT.setDisable(true);
                }

                if (textField.getText().length() > charLimit) {
                    textField.setText(textField.getText().substring(0, charLimit));
                    textField.positionCaret(textField.getText().length());
                }
            });

            textField.setAlignment(Pos.CENTER);
            textField.setMaxWidth(200);

            btnRankJSON.setOnAction(e -> {
                this.rank = new RankingJSON();
                this.rank.save(new Ranking(textField.getText(), this.getPlayersPoints()));
                this.resetGame();
                this.soundListener.stopAll();
                fxglGameController.gotoMainMenu();
            });
            btnRankTXT.setOnAction(e -> {
                this.rank = new RankingTXT();
                this.rank.save(new Ranking(textField.getText(), this.getPlayersPoints()));
                this.resetGame();
                this.soundListener.stopAll();
                fxglGameController.gotoMainMenu();
            });

            fxglDialogService.showBox(msg, textField, btnRankJSON, btnRankTXT);

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
        this.players.put(PlayerTypes.P1, null);
        if (isMultiplayer)
            this.players.put(PlayerTypes.P2, null);

        this.resetSpawn();
        this.currentLevel = 0;
        this.fxglWorld.reset();
//        this.fxglGameController.startNewGame();

    }

    @Override
    public void playerBulletHittingEnemy(PlayerTypes playerThatHitted) {
        Entity player = this.players.get(playerThatHitted);

        player.getComponent(Player.class).hit();
        double points = player.getComponent(Player.class).getPoints();

        this.setPlayerUIInformation();

        System.out.println(playerThatHitted + ": " + points);
    }


    private void updateUiInformation() {
//        this.textPixels.setText("Pontuação: " + String.format("%.0f", points));
    }

    private void playRandomMusic() {
        this.soundListener.stopAllMusics();
        MusicsNames[] musicsNames = MusicsNames.values();
        MusicsNames someRandomMusic = musicsNames[this.random.nextInt(musicsNames.length)];
        System.out.println("Playing Music: " + someRandomMusic);

        this.soundListener.playMusic(someRandomMusic);
    }

    @Override
    public void spawnEnemy() {
        EnemyType[] enemyTypes = EnemyType.values();
        EnemyType enemyType = enemyTypes[this.random.nextInt(enemyTypes.length)];

        if ((System.currentTimeMillis() - lastSpawn) > spawnTimer && !this.playerCanLevelUp() && System.currentTimeMillis() - this.enterLevelTime > 5000) {

            PlayerTypes[] playerTypes = PlayerTypes.values();
            PlayerTypes playerType = this.isMultiplayer ? playerTypes[this.random.nextInt(playerTypes.length)] : PlayerTypes.P1;
            System.out.println("Spawn enemy following " + playerType);

            Entity playerToFollow = this.getPlayer(playerType);

            switch (this.random.nextInt(4)) {
                case 0:
                    gameFactory.newEnemy(30, 360, enemyType, playerToFollow);
                    break;

                case 1:
                    gameFactory.newEnemy(1200, 360, enemyType, playerToFollow);
                    break;

                case 2:
                    gameFactory.newEnemy(640, 640, enemyType, playerToFollow);
                    break;

                case 3:
                    gameFactory.newEnemy(640, 30, enemyType, playerToFollow);
                    break;
            }

            this.lastSpawn = System.currentTimeMillis();
        }

        elapsedTime = elapsedTime + 10;

        if (this.elapsedTime > 2000 && this.spawnTimer > 500) {
            // Para tornar o jogo mais difícil, pode-se alterar o passo em que diminui-se
            // o spawn timer
            this.spawnTimer = this.spawnTimer - 50;
            this.elapsedTime = 0;
        }
    }


    private void resetSpawn() {
        this.spawnTimer = 2000;
        this.lastSpawn = System.currentTimeMillis();
    }

    @Override
    public void setPlayerUIInformation() {
        this.playersPointsUI.setText("Pontuação: " + String.format("%.0f", this.getPlayersPoints()));

        Entity p1 = this.players.get(PlayerTypes.P1);
        PowerType p1Power = p1.getComponent(Player.class).getPowerType();

        String p1Text = "P1 Ultimo Poder: ";


        if(p1Power != null)
            p1Text += p1Power;

        String playersLife = "Vidas P1: " + p1.getComponent(Player.class).getLife() + "           ";
        this.p1ActivePowerUI.setText(p1Text);

        if(this.isMultiplayer) {
            Entity p2 = this.players.get(PlayerTypes.P2);
            PowerType p2Power = p2.getComponent(Player.class).getPowerType();

            String p2Text = "P2 Ultimo Poder: ";

            if(p2Power != null)
                p2Text += p2Power;

            playersLife += "Vidas P2: " + p2.getComponent(Player.class).getLife();
            this.p2ActivePowerUI.setText(p2Text);
        }

        this.playersLifeUI.setText(playersLife);
    }

    @Override
    public void initPlayerUIInfo() {
        this.playersPointsUI.setTranslateX(18);
        this.playersPointsUI.setTranslateY(32);

        this.p1ActivePowerUI.setTranslateX(18);
        this.p1ActivePowerUI.setTranslateY(this.mapHeight - 18);

        this.p2ActivePowerUI.setTranslateX(this.mapWidth - 270);
        this.p2ActivePowerUI.setTranslateY(this.mapHeight - 18);

        this.playersLifeUI.setTranslateX(this.mapWidth - 270);
        this.playersLifeUI.setTranslateY(32);

        Font font = new Font(20);

        this.playersPointsUI.setFont(font);
        this.p1ActivePowerUI.setFont(font);
        this.p2ActivePowerUI.setFont(font);
        this.playersLifeUI.setFont(font);

        this.fxglGameScene.addUINode(this.playersPointsUI);
        this.fxglGameScene.addUINode(this.p1ActivePowerUI);
        this.fxglGameScene.addUINode(this.p2ActivePowerUI);
        this.fxglGameScene.addUINode(this.playersLifeUI);
    }
    private void initPlayerOptionToSave() {
        String msg = "Escolha o tipo de Save:";

        Button btnRankJSON = this.fxglFactoryService.newButton("Save JSON");
        Button btnRankTXT = this.fxglFactoryService.newButton("Save TXT");


        btnRankJSON.setAlignment(Pos.CENTER);
        btnRankTXT.setAlignment(Pos.CENTER);

        btnRankJSON.setTranslateX(-10);
        btnRankTXT.setTranslateX(10);

        Rectangle a = new Rectangle(1,1);

        btnRankJSON.setOnAction(e -> {
            this.savingWorld(new SaveJSON());
        });
        btnRankTXT.setOnAction(e -> {
            this.savingWorld(new SaveTXT());
        });

        this.fxglDialogService.showBox(msg, a, btnRankJSON, btnRankTXT);

    }

    private void savingWorld(SaveDAO save) {
        this.enterLevelTime = System.currentTimeMillis();
        Save save1 = new Save(
                isMultiplayer,
                currentLevel,
                players
        );

        save.save(save1);

    }
}




