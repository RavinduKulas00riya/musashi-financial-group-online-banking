package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "interests")
public class Interest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Interest() {
    }

    private Double amount;

    public Interest(Integer id, Double balanceAfter, Account account, LocalDateTime dateTime, Double amount) {
        this.id = id;
        this.balanceAfter = balanceAfter;
        this.account = account;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accounts_id")
    private Account account;

    @Column(name = "balance_after")
    private Double balanceAfter;

    // Getters and setters
}