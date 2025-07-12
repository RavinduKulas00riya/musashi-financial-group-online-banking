package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.InterestService;
import lk.jiat.app.core.service.NotificationService;

import java.util.List;

@Stateless
public class InterestSessionBean implements InterestService {

    @EJB
    private NotificationService notificationService;

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addInterest(Interest interest) {
        em.persist(interest);
        notificationService.sendNotification(
                new Notification("$"+interest.getAmount()+" of daily interest has been added to your balance.",
                        interest.getAccount().getUser(),interest.getDateTime()));
    }

    @Override
    public List<Interest> getInterests(Account account) {

        try {
            return em.createNamedQuery("Interest.findInterestsByAccount", Interest.class)
                    .setParameter("account", account).getResultList();
        }catch (NoResultException e){
            return List.of();
        }

    }
}
