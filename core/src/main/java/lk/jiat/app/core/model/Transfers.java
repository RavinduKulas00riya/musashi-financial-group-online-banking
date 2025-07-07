package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfers")
public class Transfers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Transfers() {
    }

    @ManyToOne
    @JoinColumn(name = "from_acc")
    private Accounts fromAccount;

    public Transfers(Integer id, LocalDateTime dateTime, TransactionStatus transactionStatus, Double amount, Accounts toAccount, Accounts fromAccount) {
        this.id = id;
        this.dateTime = dateTime;
        this.transactionStatus = transactionStatus;
        this.amount = amount;
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
    }

    @ManyToOne
    @JoinColumn(name = "to_acc")
    private Accounts toAccount;

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

    public Accounts getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(Accounts fromAccount) {
        this.fromAccount = fromAccount;
    }

    public Accounts getToAccount() {
        return toAccount;
    }

    public void setToAccount(Accounts toAccount) {
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
