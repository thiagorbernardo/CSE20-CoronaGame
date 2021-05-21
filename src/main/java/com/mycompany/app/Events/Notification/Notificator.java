package com.mycompany.app.Events.Notification;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.notification.NotificationService;
import javafx.scene.paint.Color;

public class Notificator implements NotificationListener {
    private NotificationService notify = FXGL.getNotificationService();

    @Override
    public void fireEvent(Color color, String msg) {

        this.notify.setBackgroundColor(color);
        this.notify.setTextColor(Color.RED);
        this.notify.pushNotification(msg);

    }
}