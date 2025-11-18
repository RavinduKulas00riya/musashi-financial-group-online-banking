package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.event.PublicEvent;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.ScheduledTransactionService;

import java.text.DecimalFormat;
import java.util.List;

@Stateless
public class ScheduledTransactionSessionBean implements ScheduledTransactionService {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private NotificationService notificationService;

    @Inject
    private Event<PublicEvent> publicEvent;

    @Override
    public void createTransaction(ScheduledTransfer transfer) {
        em.persist(transfer);
    }

    @Override
    public List<ScheduledTransfer> getTransactions(Account customer) {
        return List.of();
    }

    @Override
    public List<ScheduledTransfer> getTransactions(Account account1, Account account2) {
        return List.of();
    }

    @Override
    public List<ScheduledTransfer> getAllTransactions() {
        try {
            return em.createNamedQuery("ScheduledTransfer.findAll", ScheduledTransfer.class).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }

    @Override
    public void deleteTransaction(ScheduledTransfer transfer) {
        em.remove(transfer);
    }

    @Override
    public void updateTransaction(ScheduledTransfer transfer) {
        em.merge(transfer);
        DecimalFormat df = new DecimalFormat("#0.00");
        if(transfer.getStatus().equals(ScheduledTransactionStatus.COMPLETED)){

            notificationService.sendNotification(new Notification(
                    "$"+df.format(transfer.getAmount())+" has been transferred to you by "+transfer.getFromAccount().getAccountNo(),
                    transfer.getToAccount().getUser(),
                    transfer.getDateTime()));
            notificationService.sendNotification(new Notification(
                    "The scheduled transaction to send $"+df.format(transfer.getAmount())+" to "+transfer.getToAccount().getAccountNo()+" has been completed",
                    transfer.getFromAccount().getUser(),
                    transfer.getDateTime()));
//            publicEvent.fire(new PublicEvent(transfer.getToAccount().getUser().getId()));
//            publicEvent.fire(new PublicEvent(transfer.getFromAccount().getUser().getId()));
        }else{
            notificationService.sendNotification(new Notification(
                    "The scheduled transaction to send $"+df.format(transfer.getAmount())+" to "+transfer.getToAccount().getAccountNo()+" has been cancelled (Paused by user)",
                    transfer.getFromAccount().getUser(),
                    transfer.getDateTime()));
//            publicEvent.fire(new PublicEvent(transfer.getFromAccount().getUser().getId()));
        }

    }

    @Override
    public List<ScheduledTransfer> getTransactionsByStatus(ScheduledTransactionStatus status) {
        try {
            return em.createNamedQuery("ScheduledTransfer.findByStatus", ScheduledTransfer.class).setParameter("status",status).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }
}
