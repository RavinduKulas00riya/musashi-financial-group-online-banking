package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import lk.jiat.app.core.event.PublicEvent;
import lk.jiat.app.core.model.*;
import lk.jiat.app.core.service.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
@Startup
public class TimerSessionBean implements TimerService {

    @EJB
    private AccountService accountService;

    @EJB
    private ScheduledTransactionService scheduledTransactionService;

    @EJB
    private TransactionService transactionService;

    @EJB
    private DailyBalanceService dailyBalanceService;

    @Inject
    private Event<PublicEvent> publicEvent;

    private final Double interestRate = 0.001;

    @Override
    @Schedule(hour = "*", minute = "*", second = "0", persistent = true)
    public void checkPendingTransactions() {

        try {

            List<ScheduledTransfer> pendingTransactions = scheduledTransactionService.getTransactionsByStatus(ScheduledTransactionStatus.PENDING);
            pendingTransactions.forEach(t -> {

                if (t.getDateTime().isBefore(LocalDateTime.now())) {

                    Double amount = t.getAmount();
                    Account from = t.getFromAccount();
                    Account to = t.getToAccount();
                    from.setBalance(from.getBalance() - amount);
                    to.setBalance(to.getBalance() + amount);
                    accountService.updateAccount(from);
                    accountService.updateAccount(to);
                    t.setStatus(ScheduledTransactionStatus.COMPLETED);
                    scheduledTransactionService.updateTransaction(t);
                    transactionService.createTransaction(new Transfer(t.getDateTime(),t.getAmount(),t.getToAccount(),t.getFromAccount()));
                }
            });
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    @Schedule(hour = "*", minute = "*", second = "0", persistent = true)
    public void checkPausedTransactions() {
        List<ScheduledTransfer> pausedTransactions = scheduledTransactionService.getTransactionsByStatus(ScheduledTransactionStatus.PAUSED);
        pausedTransactions.forEach(t -> {

            if (t.getDateTime().isBefore(LocalDateTime.now())) {
                t.setStatus(ScheduledTransactionStatus.CANCELLED);
                scheduledTransactionService.updateTransaction(t);
            }
        });
    }

    @Override
    @Schedule(hour = "0", minute = "0", second = "0", persistent = true)
    public void dailyInterestCalculation() {

        try {

            List<Account> accountList = accountService.getActiveAccounts();

            accountList.forEach(account -> {

                Double oldBalance = account.getBalance();
                Double amount = account.getBalance() * interestRate;
                Double formattedAmount = BigDecimal.valueOf(amount)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                Double newBalance = oldBalance + formattedAmount;
                Double finalBalance = BigDecimal.valueOf(newBalance)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();

                account.setBalance(finalBalance);
                accountService.updateAccount(account);
                Account interestAccount = accountService.getAccount("000000000");
                transactionService.createTransaction(new Transfer(LocalDateTime.now(),formattedAmount,account,interestAccount));
                publicEvent.fire(new PublicEvent(account.getUser().getId()));
            });
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    @Schedule(hour = "0", minute = "0", second = "0", persistent = true)
    public void dailyBalanceUpdate() {
        LocalDate beforeYesterday = LocalDate.now().minusDays(2);
        DailyBalance beforeYesterdayBalance = dailyBalanceService.getDailyBalanceByDate(beforeYesterday);

        if(beforeYesterdayBalance==null) return;

        Double oldBalance = beforeYesterdayBalance.getBalance();

        Double currentBalance = 0.0;

        List<Account> accountList = accountService.getAllAccounts();

        for (Account account : accountList) {
            currentBalance += account.getBalance();
        }

        String profit = calculateProfit(oldBalance, currentBalance);

        dailyBalanceService.addDailyBalance(new DailyBalance(LocalDate.now().minusDays(1),currentBalance,profit));

    }

    private String calculateProfit(Double before, Double after) {
        double change = ((after - before) / before) * 100;

        String sign = (change > 0) ? "+" : (change < 0) ? "" : "";
        return sign + String.format("%.2f", change) + "%";
    }
}
