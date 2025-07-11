package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.AccountStatus;
import lk.jiat.app.core.model.User;

import java.util.List;

@Remote
public interface AccountService {
    Account getAccount(String AccountNumber);
    void updateAccount(Account account);
    List<Account> getAllAccounts();
    List<Account> getActiveAccounts();
}
