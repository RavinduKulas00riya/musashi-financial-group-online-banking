package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transaction;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.TransactionService;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                    .setParameter("customer", customer).setParameter("status",TransactionStatus.COMPLETED).setMaxResults(1).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Transfer> getTransactionsByAccountAndStatusAndDateRange(Account account,TransactionStatus status, LocalDate start, LocalDate end, String sortBy) {
        LocalDateTime startDate = start != null ? start.atStartOfDay() : null;
        LocalDateTime endDate = end != null ? end.plusDays(1).atStartOfDay() : null;
        String baseQuery = "SELECT t FROM Transfer t " +
                "WHERE t.transactionStatus = :status " +
                "AND (t.toAccount = :account OR t.fromAccount = :account) " +
                "AND (:startDate IS NULL OR t.dateTime >= :startDate) " +
                "AND (:endDate IS NULL OR t.dateTime <= :endDate) ";

        String orderClause;

        switch (sortBy) {
            case "dateAsc":
                orderClause = "ORDER BY t.dateTime ASC";
                break;
            case "dateDesc":
                orderClause = "ORDER BY t.dateTime DESC";
                break;
            case "amountAsc":
                orderClause = "ORDER BY t.amount ASC";
                break;
            case "amountDesc":
                orderClause = "ORDER BY t.amount DESC";
                break;
            default:
                orderClause = "ORDER BY t.dateTime DESC";
                break;
        }

        TypedQuery<Transfer> query = em.createQuery(baseQuery + orderClause, Transfer.class);
        query.setParameter("status", status);
        query.setParameter("account", account);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    @Override
    public Transfer getLatestSent(Account customer) {
        try {
            return em.createNamedQuery("Transfer.findLatestSent", Transfer.class)
                    .setParameter("customer", customer).setParameter("status",TransactionStatus.COMPLETED).setMaxResults(1).getSingleResult();
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
    public List<Transfer> getTransactionsByStatus(TransactionStatus status) {
        try {
            return em.createNamedQuery("Transfer.findTransactionsByStatus", Transfer.class)
                    .setParameter("status", status).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }

    @Override
    public void updateTransaction(Transfer transfer) {
        em.merge(transfer);
        DecimalFormat df = new DecimalFormat("#0.00");
        notificationService.sendNotification(new Notification(
                "$"+df.format(transfer.getAmount())+" has been transferred to you by "+transfer.getFromAccount().getAccountNo(),
                transfer.getToAccount().getUser(),
                transfer.getDateTime()));
        notificationService.sendNotification(new Notification(
                "The scheduled transaction to send $"+df.format(transfer.getAmount())+" to "+transfer.getToAccount().getAccountNo()+" has been completed",
                transfer.getFromAccount().getUser(),
                transfer.getDateTime()));
    }
}
