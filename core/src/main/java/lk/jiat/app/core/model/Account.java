package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_no")
    private String accountNo;

    private Double balance;

    public Account(Integer id, String accountNo, Double balance, User user, LocalDateTime createdDateTime, List<Interest> interests) {
        this.id = id;
        this.accountNo = accountNo;
        this.balance = balance;
        this.user = user;
        this.createdDateTime = createdDateTime;
        this.interests = interests;
    }

    public Account() {
    }

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void setInterests(List<Interest> interests) {
        this.interests = interests;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Interest> interests;

    // Getters and setters
}
