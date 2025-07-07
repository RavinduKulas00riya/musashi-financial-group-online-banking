package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notifications implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Notifications() {
    }

    private String message;

    public Notifications(Integer id, String message, Users user, LocalDateTime dateTime) {
        this.id = id;
        this.message = message;
        this.user = user;
        this.dateTime = dateTime;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    // Getters and setters
}
