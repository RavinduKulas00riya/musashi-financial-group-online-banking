package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.dto.CustomerTimelyOperationsTableDTO;
import lk.jiat.app.core.dto.CustomerTransactionHistoryTableDTO;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.ScheduledTransactionStatus;
import lk.jiat.app.core.model.ScheduledTransfer;
import lk.jiat.app.core.model.Transfer;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    CustomerTimelyOperationsTableDTO customerTimelyOperationsTable(Account account, LocalDateTime scheduledStart, LocalDateTime scheduledEnd, LocalDateTime createdStart, LocalDateTime createdEnd, String sortBy, int page, int pageSize, String accountNum, String counterparty, ScheduledTransactionStatus status);
    ScheduledTransfer getTransactionById(Integer id);
}
