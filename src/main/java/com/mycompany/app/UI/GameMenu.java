package com.mycompany.app.UI;

import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.mycompany.app.UI.Menu;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import static javafx.beans.binding.Bindings.createStringBinding;

public class GameMenu extends Menu {

    public GameMenu(MenuType type) {
        super(type);
    }

    @Override
    public Node createBody() {

        Node btn0 = this.createActionButton(createStringBinding(() -> "RESUME"), this::fireResume);
        Node btn1 = this.createActionButton(createStringBinding(() -> "SAVE"), this::fireResume);
        Node btn2 = this.createActionButton(createStringBinding(() -> "MAIN MENU"), this::fireExit);


        Group group = new Group(btn0, btn1, btn2);

        int i = 0;
        for (Node n : group.getChildren()) {

            Point2D vector = new Point2D(100, 225);
//                    .normalize()

            n.setLayoutX(vector.getX());
            n.setLayoutY(vector.getY() + i * 75);
            i++;
        }

        return group;
    }

}