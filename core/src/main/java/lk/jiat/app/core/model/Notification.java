package lk.jiat.app.core.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NamedQueries({
        @NamedQuery(name = "Notification.markAllAsSeen", query = "UPDATE Notification n SET n.status = :status WHERE n.user = :user")
})
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Notification() {
    }

    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.SENT;
    private String message;

    public Notification(String message, User user, LocalDateTime dateTime) {
        this.message = message;
        this.user = user;
        this.dateTime = dateTime;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
    private User user;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

}
