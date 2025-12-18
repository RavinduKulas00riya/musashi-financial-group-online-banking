package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.dto.CustomerTransactionHistoryTableDTO;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.Transfer;

import java.time.LocalDate;
import java.util.List;

@Remote
public interface TransactionService {
    void createTransaction(Transfer transfer);
    List<Transfer> getTransactions(Account customer);
    List<Transfer> getTransactions(Account account1, Account account2);
    List<Transfer> getAllTransactions();
    void deleteTransaction(Transfer transfer);
    Transfer getTransaction(Integer id);
    void updateTransaction(Transfer transfer);
    Transfer getLatestSent(Account customer);
    Transfer getLatestReceived(Account customer);
    CustomerTransactionHistoryTableDTO customerTransactionHistoryTable(Account account, LocalDate start, LocalDate end, String sortBy, int page, int pageSize, String accountNum, String counterparty, String type);
}
