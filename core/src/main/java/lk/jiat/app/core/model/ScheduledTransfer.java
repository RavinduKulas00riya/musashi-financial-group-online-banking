package lk.jiat.app.core.model;

import jakarta.persistence.*;
import lk.jiat.app.core.service.ScheduledTransactionService;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "scheduled_transfers")
@NamedQueries({
        @NamedQuery(name = "ScheduledTransfer.findAll", query = "select t from ScheduledTransfer t order by t.dateTime DESC"),
        @NamedQuery(name = "ScheduledTransfer.findByStatus", query = "select t from ScheduledTransfer t where t.status=:status"),
})
public class ScheduledTransfer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "from_acc")
    private Account fromAccount;

    public ScheduledTransactionStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduledTransactionStatus status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "to_acc")
    private Account toAccount;

    private Double amount;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private ScheduledTransactionStatus status = ScheduledTransactionStatus.PENDING;

    private LocalDateTime created_datetime = LocalDateTime.now();

    public ScheduledTransfer(LocalDateTime dateTime, Double amount, Account toAccount, Account fromAccount) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.toAccount = toAccount;
        this.fromAccount = fromAccount;
    }

    public ScheduledTransfer() {

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

    public LocalDateTime getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(LocalDateTime created_datetime) {
        this.created_datetime = created_datetime;
    }
}
