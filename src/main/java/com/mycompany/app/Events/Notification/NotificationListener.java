package com.mycompany.app.Events.Notification;

import javafx.scene.paint.Color;

public interface NotificationListener {
    void fireEvent(Color color, String msg);
}
