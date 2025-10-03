package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transaction;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.TransactionService;

import java.util.List;

@Stateless
public class TransactionSessionBean implements TransactionService {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private NotificationService notificationService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createTransaction(Transfer transfer) {
        em.persist(transfer);
    }

    @Override
    public List<Transfer> getTransactions(Account customer) {
        try {
            return em.createNamedQuery("Transfer.findByUser", Transfer.class)
                    .setParameter("customer", customer).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }

    @Override
    public List<Transfer> getTransactions(Account account1, Account account2) {
        try {
            return em.createNamedQuery("Transfer.findByUsers", Transfer.class)
                    .setParameter("account1", account1).setParameter("account2",account2).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }

    @Override
    public List<Transfer> getAllTransactions() {
        try {
            return em.createNamedQuery("Transfer.findAll", Transfer.class).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }

    @Override
    public Transfer getLatestReceived(Account customer) {
        try {
            return em.createNamedQuery("Transfer.findLatestReceived", Transfer.class)
                    .setParameter("customer", customer).setMaxResults(1).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Transfer getLatestSent(Account customer) {
        try {
            return em.createNamedQuery("Transfer.findLatestSent", Transfer.class)
                    .setParameter("customer", customer).setMaxResults(1).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void deleteTransaction(Transfer transfer) {
        Transfer managedTransfer = em.merge(transfer);
        em.remove(managedTransfer);
    }

    @Override
    public Transfer getTransaction(Integer id) {
        return em.find(Transfer.class, id);
    }

    @Override
    public List<Transfer> getPendingTransactions() {
        try {
            return em.createNamedQuery("Transfer.findPendingTransactions", Transfer.class)
                    .setParameter("status", TransactionStatus.PENDING).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }

    @Override
    public void updateTransaction(Transfer transfer) {
        em.merge(transfer);
        notificationService.sendNotification(new Notification(
                "$"+transfer.getAmount()+" has been transferred to you by "+transfer.getFromAccount().getAccountNo(),
                transfer.getToAccount().getUser(),
                transfer.getDateTime()));
        notificationService.sendNotification(new Notification(
                "The scheduled transaction to send $"+transfer.getAmount()+" to "+transfer.getToAccount().getAccountNo()+" has been completed",
                transfer.getFromAccount().getUser(),
                transfer.getDateTime()));
    }
}
