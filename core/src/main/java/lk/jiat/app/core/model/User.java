package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "select u from User u where u.email =:email"),
        @NamedQuery(name = "User.findByMobile", query = "select u from User u where u.mobile =:mobile"),
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String mobile;
    private String email;
    private String password;

    public User() {
    }

    public User(String name, String mobile, String email, String password, List<Account> accounts, List<Notification> notifications) {
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

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
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
    private List<Account> accounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    // Getters and setters
}
