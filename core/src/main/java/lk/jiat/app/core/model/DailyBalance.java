package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_balance")
@NamedQueries({
        @NamedQuery(name = "DailyBalance.findAllRecords", query = "select d from DailyBalance d order by d.date desc"),
        @NamedQuery(name = "DailyBalance.findByDate", query = "select d from DailyBalance d where d.date=:date"),
})
public class DailyBalance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "balance_date")
    private LocalDate date;

    public DailyBalance() {
    }

    public DailyBalance(LocalDate date, Double balance, String profit) {
        this.date = date;
        this.balance = balance;
        this.profit = profit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateTime() {
        return date;
    }

    public void setDateTime(LocalDate date) {
        this.date = date;
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
