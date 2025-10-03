package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.model.Notification;
import lk.jiat.app.core.model.User;

import java.util.List;

@Remote
public interface NotificationService {
    void sendNotification(Notification notification);
    void markAllAsSeen(User user);
}
