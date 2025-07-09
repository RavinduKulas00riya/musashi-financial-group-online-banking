package lk.jiat.app.ejb.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.dto.LoginDto;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.UserService;
import lk.jiat.app.core.util.Encryption;

@Stateless
public class UserSessionBean implements UserService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User getUserById(Integer id) {
        try {
            User user = em.find(User.class, id);
            user.getNotifications().size();
            return user;
        }catch (NoResultException e){
            return null;
        }

    }

    @Override
    public User getUserByEmail(String email) {

        try {
            return em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public User getUserByMobile(String mobile) {

        try {
            return em.createNamedQuery("User.findByMobile", User.class)
                    .setParameter("mobile", mobile).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void addUser(User user) {
        em.persist(user);
    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public User validate(String email, String password) {

        if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {

            User user = getUserByEmail(email);

            if(user!=null && user.getPassword().equals(password)) return user;

        }

        return null;
    }
}
