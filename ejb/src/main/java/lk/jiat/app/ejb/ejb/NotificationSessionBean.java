package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.model.Notification;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.UserService;

import java.util.List;

@Stateless
public class NotificationSessionBean implements NotificationService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void sendNotification(Notification notification) {
        em.persist(notification);
    }
}
