package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Details;
import es.uniovi.miw.ws.pgg.rest.transactions.models.Group;
import es.uniovi.miw.ws.pgg.rest.transactions.models.Transaction;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsRepository extends JpaRepository<Details, Long>{

    // Método para encontrar detalles por ID de transacción
   public List<Details> findByTransactionId(Long transactionId);

    public Details findByTransactionAndUserGroup(Transaction transaction, UserGroup userGroup);

    // Método para encontrar detalles por ID de grupo de usuario
   //public   List<Details> findByUserGroupId(Long userGroupId);
}
