package lk.jiat.app.core.model;

import jakarta.persistence.*;
import lk.jiat.app.core.util.TransactionIdGenerator;

import java.io.Serializable;
import java.time.LocalDateTime;

import static lk.jiat.app.core.model.TransactionStatus.COMPLETED;

@Entity
@Table(name = "transfers")
@NamedQueries({
        @NamedQuery(name = "Transfer.findByUser", query = "select t from Transfer t where t.toAccount=:customer or t.fromAccount=:customer order by t.dateTime DESC"),
        @NamedQuery(name = "Transfer.findByUsers", query = "select t from Transfer t where (t.toAccount=:account1 and t.fromAccount=:account2) or (t.toAccount=:account2 and t.fromAccount=:account1) order by t.dateTime DESC"),
        @NamedQuery(name = "Transfer.findPendingTransactions", query = "select t from Transfer t where t.transactionStatus=:status"),
        @NamedQuery(name = "Transfer.findAll", query = "select t from Transfer t order by t.dateTime DESC"),
        @NamedQuery(
                name = "Transfer.findLatestReceived",
                query = "select t from Transfer t where t.toAccount=:customer and t.transactionStatus=:status order by t.dateTime DESC"
        ),
        @NamedQuery(
                name = "Transfer.findLatestSent",
                query = "select t from Transfer t where t.fromAccount=:customer and t.transactionStatus=:status order by t.dateTime DESC"
        )
})
public class Transfer implements Serializable {
    @Id
    private String id = TransactionIdGenerator.generateRandomString();

    public Transfer() {
    }

    @ManyToOne
    @JoinColumn(name = "from_acc")
    private Account fromAccount;

    public Transfer(LocalDateTime dateTime, TransactionStatus transactionStatus, Double amount, Account toAccount, Account fromAccount) {
        this.dateTime = dateTime;
        this.transactionStatus = transactionStatus;
        this.amount = amount;
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
    }

    @ManyToOne
    @JoinColumn(name = "to_acc")
    private Account toAccount;

    private Double amount;

    private LocalDateTime created_datetime = LocalDateTime.now();

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Account fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }

    public void setToAccount(Account toAccount) {
        this.toAccount = toAccount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public LocalDateTime getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(LocalDateTime created_datetime) {
        this.created_datetime = created_datetime;
    }

    // Getters and setters
}
