package com.mycompany.app.UI;

import com.almasb.fxgl.app.scene.MenuType;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import static javafx.beans.binding.Bindings.createStringBinding;

public class GameMenu extends Menu {

    /**
     * Creates game menu with background and body with buttons.
     * @param type menu type Game or Main
     */
    public GameMenu(MenuType type) {
        super(type);
    }

    /**
     * Creates a group of buttons
     * @return the node with buttons
     */
    @Override
    protected Node createBody() {

        Node btn0 = this.createActionButton(createStringBinding(() -> "RESUME"), this::fireResume);
        Node btn1 = this.createActionButton(createStringBinding(() -> "EXIT"), this::fireExit);


        Group group = new Group(btn0, btn1);

        int i = 0;
        for (Node n : group.getChildren()) {

            Point2D vector = new Point2D(100, 225);

            n.setLayoutX(vector.getX());
            n.setLayoutY(vector.getY() + i * 75);
            i++;
        }

        return group;
    }

}