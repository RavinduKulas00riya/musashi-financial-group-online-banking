package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.event.CustomerDashboardEvent;
import lk.jiat.app.core.model.Interest;
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
    private Event<CustomerDashboardEvent> customerDashboardEvent;

    @Override
    public void sendNotification(Notification notification) {

        em.persist(notification);
        customerDashboardEvent.fire(new CustomerDashboardEvent(notification.getUser().getId()));
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
