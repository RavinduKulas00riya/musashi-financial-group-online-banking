package lk.jiat.app.core.service;

import jakarta.ejb.Remote;

@Remote
public interface TimerService {

    void checkPendingTransactions();
    void dailyInterestCalculation();
    void dailyBalanceUpdate();
}
