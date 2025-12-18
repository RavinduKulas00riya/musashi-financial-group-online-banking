package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import lk.jiat.app.core.dto.CustomerTimelyOperationsTableDTO;
import lk.jiat.app.core.dto.CustomerTransactionHistoryTableDTO;
import lk.jiat.app.core.event.PublicEvent;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.ScheduledTransactionService;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Override
    public CustomerTimelyOperationsTableDTO customerTimelyOperationsTable(Account account, LocalDate scheduledStart, LocalDate scheduledEnd, LocalDate createdStart, LocalDate createdEnd, String sortBy, int page, int pageSize, String accountNum, String counterparty, ScheduledTransactionStatus status) {
        try {

            CustomerTimelyOperationsTableDTO result = new CustomerTimelyOperationsTableDTO();

            String whereClause = buildWhereClause(accountNum, counterparty, status);

            String orderClause = "";

            switch (sortBy) {
                case "scheduledAsc":
                    orderClause = "ORDER BY t.dateTime ASC";
                    break;
                case "scheduledDesc":
                    orderClause = "ORDER BY t.dateTime DESC";
                    break;
                case "createdAsc":
                    orderClause = "ORDER BY t.created_datetime ASC";
                    break;
                case "createdDesc":
                    orderClause = "ORDER BY t.created_datetime DESC";
                    break;
                case "amountAsc":
                    orderClause = "ORDER BY t.amount ASC";
                    break;
                case "amountDesc":
                    orderClause = "ORDER BY t.amount DESC";
                    break;
            }

            //set the main result set
            TypedQuery<ScheduledTransfer> dataQuery =
                    em.createQuery(
                            "SELECT t FROM ScheduledTransfer t " + whereClause + orderClause,
                            ScheduledTransfer.class
                    );
            bindCommonParameters(
                    dataQuery, account, scheduledStart, scheduledEnd,
                    createdStart, createdEnd, accountNum, counterparty, status
            );
            dataQuery.setFirstResult((page - 1) * pageSize);
            dataQuery.setMaxResults(pageSize);
            result.setList(dataQuery.getResultList());

            //set table's total row count
            TypedQuery<Long> countQuery =
                    em.createQuery(
                            "SELECT COUNT(t) FROM ScheduledTransfer t " + whereClause,
                            Long.class
                    );

            bindCommonParameters(
                    countQuery, account, scheduledStart, scheduledEnd,
                    createdStart, createdEnd, accountNum, counterparty, status
            );

            result.setTotalRowCountAfterFiltering(countQuery.getSingleResult().intValue());

            //get nearest transaction's date and time
            TypedQuery<LocalDateTime> dateQuery =
                    em.createQuery(
                            "SELECT t.dateTime FROM ScheduledTransfer t " +
                                    "WHERE t.fromAccount = :account " +
                                    "AND t.status <> :completedStatus " +
                                    "ORDER BY t.dateTime DESC",
                            LocalDateTime.class
                    );

            dateQuery.setParameter("account", account);
            dateQuery.setParameter("completedStatus", ScheduledTransactionStatus.COMPLETED);
            dateQuery.setMaxResults(1);
            result.setNextDateTime(dateQuery.getResultList().stream().findFirst().orElse(null));

            //get total scheduled transaction count
            TypedQuery<Long> totalCountQuery =
                    em.createQuery(
                            "SELECT COUNT(t) FROM ScheduledTransfer t " +
                                    "WHERE t.fromAccount = :account " +
                                    "AND t.status <> :completedStatus ",
                            Long.class
                    );

            totalCountQuery.setParameter("account", account);
            totalCountQuery.setParameter("completedStatus", ScheduledTransactionStatus.COMPLETED);
            result.setTotalRowCount(totalCountQuery.getSingleResult().intValue());

            //get total amount of scheduled transactions
            TypedQuery<Double> amountQuery =
                    em.createQuery(
                            "SELECT SUM(t.amount) FROM ScheduledTransfer t " +
                                    "WHERE t.fromAccount = :account " +
                                    "AND t.status <> :completedStatus ",
                            Double.class
                    );

            amountQuery.setParameter("account", account);
            amountQuery.setParameter("completedStatus", ScheduledTransactionStatus.COMPLETED);
            Double total = amountQuery.getSingleResult();
            result.setTotalAmount(total != null ? total : 0.0);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildWhereClause(
            String accountNum,
            String counterparty,
            ScheduledTransactionStatus status
    ) {
        StringBuilder sb = new StringBuilder();

        sb.append(" WHERE t.fromAccount = :account ")
                .append(" AND (:scheduledStart IS NULL OR t.dateTime >= :scheduledStart) ")
                .append(" AND (:scheduledEnd IS NULL OR t.dateTime <= :scheduledEnd) ")
                .append(" AND (:createdStart IS NULL OR t.created_datetime >= :createdStart) ")
                .append(" AND (:createdEnd IS NULL OR t.created_datetime <= :createdEnd) ")
                .append(" AND t.status <> :completedStatus ");

        if (accountNum != null && !accountNum.isEmpty()) {
            sb.append(" AND t.toAccount.accountNo = :accountNum ");
        }

        if (counterparty != null && !counterparty.isEmpty()) {
            sb.append(" AND t.toAccount.user.name LIKE CONCAT('%', :counterparty, '%') ");
        }

        if (status != null) {
            sb.append(" AND t.status = :status ");
        }

        return sb.toString();
    }

    private void bindCommonParameters(
            Query query,
            Account account,
            LocalDate scheduledStart,
            LocalDate scheduledEnd,
            LocalDate createdStart,
            LocalDate createdEnd,
            String accountNum,
            String counterparty,
            ScheduledTransactionStatus status
    ) {
        query.setParameter("account", account);
        query.setParameter("scheduledStart", scheduledStart);
        query.setParameter("scheduledEnd", scheduledEnd);
        query.setParameter("createdStart", createdStart);
        query.setParameter("createdEnd", createdEnd);
        query.setParameter("completedStatus", ScheduledTransactionStatus.COMPLETED);

        if (accountNum != null && !accountNum.isEmpty()) {
            query.setParameter("accountNum", accountNum);
        }

        if (counterparty != null && !counterparty.isEmpty()) {
            query.setParameter("counterparty", counterparty);
        }

        if (status != null) {
            query.setParameter("status", status);
        }
    }

}
