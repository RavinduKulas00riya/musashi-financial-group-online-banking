package lk.jiat.app.ejb.ejb;

import jakarta.ejb.Stateless;
import lk.jiat.app.core.model.Users;
import lk.jiat.app.core.service.UserService;

import java.util.List;

@Stateless
public class UserSessionBean implements UserService {
    @Override
    public List<Users> getAllUsers() {
        return List.of();
    }
}
