package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.model.Account;
import lk.jiat.app.core.model.Interest;

import java.util.List;

@Remote
public interface InterestService {
    void addInterest(Interest interest);
    List<Interest> getInterests(Account account);
}
