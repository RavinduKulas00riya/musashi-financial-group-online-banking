package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String mobile;
    private String email;
    private String password;

    public Users() {
    }

    public Users(Integer id, String name, String mobile, String email, String password, List<Accounts> accounts, List<Notifications> notifications) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.accounts = accounts;
        this.notifications = notifications;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notifications> notifications) {
        this.notifications = notifications;
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.CUSTOMER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Accounts> accounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notifications> notifications;

    // Getters and setters
}
