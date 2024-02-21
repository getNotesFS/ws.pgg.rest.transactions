package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Details;
import es.uniovi.miw.ws.pgg.rest.transactions.models.Transaction;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long>{

    @Query("SELECT d.transaction FROM Details d WHERE d.userGroup = :userGroup")
    List<Transaction> findTransactionsByUserGroup(UserGroup userGroup);
}
