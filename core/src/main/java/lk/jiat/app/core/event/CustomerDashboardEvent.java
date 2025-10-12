package lk.jiat.app.core.event;

public class CustomerDashboardEvent {
    private final Integer userId;

    public CustomerDashboardEvent(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}