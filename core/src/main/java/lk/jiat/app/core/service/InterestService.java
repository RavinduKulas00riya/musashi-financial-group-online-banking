package lk.jiat.app.core.service;

import jakarta.ejb.Remote;
import lk.jiat.app.core.model.Interest;

@Remote
public interface InterestService {
    void addInterest(Interest interest);
}
