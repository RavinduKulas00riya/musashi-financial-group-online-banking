package lk.jiat.app.ejb.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.app.core.model.DailyBalance;
import lk.jiat.app.core.model.Transfer;
import lk.jiat.app.core.service.DailyBalanceService;

import java.time.LocalDate;
import java.util.List;

@Stateless
public class DailyBalanceSessionBean implements DailyBalanceService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<DailyBalance> getAllDailyBalance() {
        try {
            return em.createNamedQuery("DailyBalance.findAllRecords", DailyBalance.class).getResultList();
        }catch (NoResultException e){
            return List.of();
        }
    }

    @Override
    public List<DailyBalance> getDailyBalanceByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public void addDailyBalance(DailyBalance dailyBalance) {

    }
}
