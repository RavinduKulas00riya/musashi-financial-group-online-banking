package lk.jiat.app.core.dto;

import lk.jiat.app.core.model.User;

import java.io.Serializable;

public class LoginDto implements Serializable {

    private boolean success;
    private User user;

    public LoginDto() {
    }

    public LoginDto(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
