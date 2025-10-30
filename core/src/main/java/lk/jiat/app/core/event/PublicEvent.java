package lk.jiat.app.core.event;

public class PublicEvent {
    private final Integer userId;

    public PublicEvent(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}