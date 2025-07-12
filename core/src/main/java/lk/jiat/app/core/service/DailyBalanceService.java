package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.model.DailyBalance;

import java.time.LocalDate;
import java.util.List;

@Remote
public interface DailyBalanceService {
    List<DailyBalance> getAllDailyBalance();
    List<DailyBalance> getDailyBalanceByDate(LocalDate date);
    void addDailyBalance(DailyBalance dailyBalance);
}
