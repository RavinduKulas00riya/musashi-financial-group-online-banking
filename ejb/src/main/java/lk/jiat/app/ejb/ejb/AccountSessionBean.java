package lk.jiat.app.ejb.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.AccountStatus;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.AccountService;

import java.util.List;

@Stateless
public class AccountSessionBean implements AccountService {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Account getAccount(String AccountNumber) {
        try {
            Account account = em.createNamedQuery("Account.findByAccountNumber", Account.class)
                    .setParameter("accountNo", AccountNumber).getSingleResult();
//            account.getUser();
//            account.getInterests().size();
            if(account.getStatus().equals(AccountStatus.ACTIVE)) return account;
        }catch (NoResultException e){
            return null;
        }
        return null;
    }

    @Override
    public void updateAccount(Account account) {
        em.merge(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return em.createNamedQuery("Account.findAll", Account.class).getResultList();
    }

    @Override
    public List<Account> getActiveAccounts() {
        try {
            return em.createNamedQuery("Account.findActiveAccounts", Account.class)
                    .setParameter("status", AccountStatus.ACTIVE).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }
}
