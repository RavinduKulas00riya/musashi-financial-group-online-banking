package lk.jiat.app.ejb.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.Interest;
import lk.jiat.app.core.service.AccountService;
import lk.jiat.app.core.service.InterestService;
import lk.jiat.app.core.service.NotificationService;
import lk.jiat.app.core.service.TimerService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
@Startup
public class TimerSessionBean implements TimerService {

    @EJB
    private AccountService accountService;

    @EJB
    private InterestService interestService;

    private final Double interestRate = 0.001;

    @Override
    @Schedule(hour = "*", minute = "*", second = "0", persistent = true)
    public void checkPendingTransactions() {
        System.out.println("Checking pending transactions at " + LocalDateTime.now());
    }

    @Override
    @Schedule(hour = "0", minute = "0", second = "0", persistent = true)
    public void dailyInterestCalculation() {

        System.out.println("Daily interest calculation at " + LocalDateTime.now());

        try {

            List<Account> accountList = accountService.getActiveAccounts();

            System.out.println("Active accounts: " + accountList.size());

            accountList.forEach(account -> {

                Double oldBalance = account.getBalance();
                Double amount = account.getBalance() * interestRate;
                Double formattedAmount = BigDecimal.valueOf(amount)
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
                Double newBalance = oldBalance + formattedAmount;

                account.setBalance(newBalance);
                accountService.updateAccount(account);
                interestService.addInterest(new Interest(newBalance, account, LocalDateTime.now(), formattedAmount));
                System.out.println("New balance: " + newBalance);

            });
            System.out.println("Daily interest calculation completed");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
