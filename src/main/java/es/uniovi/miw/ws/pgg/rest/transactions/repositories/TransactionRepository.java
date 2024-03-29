package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    public List<Transaction> findById(long id);

    public List<Transaction> findByUserId(long userId);

    public List<Transaction> findByGroupId(long groupId);
}
