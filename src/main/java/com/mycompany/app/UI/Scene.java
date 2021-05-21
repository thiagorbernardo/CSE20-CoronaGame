package com.mycompany.app.UI;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;

public class Scene extends SceneFactory {

    private Menu menu;
    private GameMenu gameMenu;

    @Override
    public FXGLMenu newMainMenu() {
        this.menu = new Menu(MenuType.MAIN_MENU);
        return this.menu;
    }

    @Override
    public FXGLMenu newGameMenu() {
        this.gameMenu = new GameMenu(MenuType.GAME_MENU);
        return this.gameMenu;
    }

    public Menu getMainMenu() {
        return this.menu;
    }

    public GameMenu getGameMenu() {
        return this.gameMenu;
    }
}