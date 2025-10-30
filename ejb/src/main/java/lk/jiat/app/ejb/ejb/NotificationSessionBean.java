package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.event.PublicEvent;
import lk.jiat.app.core.model.Notification;
import lk.jiat.app.core.model.NotificationStatus;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.UserService;

import java.util.List;

@Stateless
public class NotificationSessionBean implements NotificationService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private Event<PublicEvent> publicEvent;

    @Override
    public void sendNotification(Notification notification) {

        em.persist(notification);
        publicEvent.fire(new PublicEvent(notification.getUser().getId()));
    }

    @Override
    public void markAllAsSeen(User user) {
        try {
            em.createNamedQuery("Notification.markAllAsSeen")
                    .setParameter("user", user).setParameter("status", NotificationStatus.SEEN).executeUpdate();
            em.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
