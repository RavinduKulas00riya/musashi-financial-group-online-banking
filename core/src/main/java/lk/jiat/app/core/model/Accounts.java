package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Accounts implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_no")
    private String accountNo;

    private Double balance;

    public Accounts(Integer id, String accountNo, Double balance, Users user, LocalDateTime createdDateTime, List<Interests> interests) {
        this.id = id;
        this.accountNo = accountNo;
        this.balance = balance;
        this.user = user;
        this.createdDateTime = createdDateTime;
        this.interests = interests;
    }

    public Accounts() {
    }

    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Interests> getInterests() {
        return interests;
    }

    public void setInterests(List<Interests> interests) {
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
    private Users user;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Interests> interests;

    // Getters and setters
}
