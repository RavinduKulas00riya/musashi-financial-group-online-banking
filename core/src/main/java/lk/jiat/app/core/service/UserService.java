package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.model.Users;

import java.util.List;

@Remote
public interface UserService {
    List<Users> getAllUsers();
}
