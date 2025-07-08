package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.dto.LoginDto;
import lk.jiat.app.core.model.User;

@Remote
public interface UserService {
    User getUserById(Integer id);
    User getUserByEmail(String email);
    User getUserByMobile(String mobile);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(User user);
    User validate(String email, String password);
}
