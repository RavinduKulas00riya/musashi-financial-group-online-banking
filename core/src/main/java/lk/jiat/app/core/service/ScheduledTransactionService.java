package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.ScheduledTransactionStatus;
import lk.jiat.app.core.model.ScheduledTransfer;
import lk.jiat.app.core.model.Transfer;

import java.util.List;

@Remote
public interface ScheduledTransactionService {
    void createTransaction(ScheduledTransfer transfer);
    List<ScheduledTransfer> getTransactions(Account customer);
    List<ScheduledTransfer> getTransactions(Account account1, Account account2);
    List<ScheduledTransfer> getAllTransactions();
    void deleteTransaction(ScheduledTransfer transfer);
    void updateTransaction(ScheduledTransfer transfer);
    List<ScheduledTransfer> getTransactionsByStatus(ScheduledTransactionStatus status);
}
