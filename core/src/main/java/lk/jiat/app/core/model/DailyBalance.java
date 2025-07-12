package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_balance")
@NamedQueries({
        @NamedQuery(name = "DailyBalance.findAllRecords", query = "select d from DailyBalance d"),
})
public class DailyBalance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public DailyBalance() {
    }

    public DailyBalance(LocalDateTime dateTime, Double balance, String profit) {
        this.dateTime = dateTime;
        this.balance = balance;
        this.profit = profit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    private Double balance;
    private String profit;
}
