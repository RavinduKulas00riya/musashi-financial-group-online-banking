package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import jakarta.transaction.Transaction;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.Transfer;
import lk.jiat.app.core.model.User;

import java.util.List;

@Remote
public interface TransactionService {
    void createTransaction(Transfer transfer);
    List<Transfer> getTransactions(Account customer);
    List<Transfer> getAllTransactions();
    void deleteTransaction(Transfer transfer);
    Transfer getTransaction(Integer id);
    List<Transfer> getPendingTransactions();
    void updateTransaction(Transfer transfer);
}
