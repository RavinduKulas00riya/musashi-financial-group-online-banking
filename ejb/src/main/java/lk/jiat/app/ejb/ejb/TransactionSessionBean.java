package lk.jiat.app.ejb.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transaction;
import lk.jiat.app.core.model.Transfer;
import lk.jiat.app.core.model.User;
import lk.jiat.app.core.service.TransactionService;

import java.util.List;

@Stateless
public class TransactionSessionBean implements TransactionService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createTransaction(Transfer transfer) {
        em.persist(transfer);
    }

    @Override
    public List<Transaction> getTransactions(User user) {
        return List.of();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return List.of();
    }
}
