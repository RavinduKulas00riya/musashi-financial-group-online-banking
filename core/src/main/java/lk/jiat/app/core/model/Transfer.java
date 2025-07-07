package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Transfer() {
    }

    @ManyToOne
    @JoinColumn(name = "from_acc")
    private Account fromAccount;

    public Transfer(Integer id, LocalDateTime dateTime, TransactionStatus transactionStatus, Double amount, Account toAccount, Account fromAccount) {
        this.id = id;
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

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    // Getters and setters
}
