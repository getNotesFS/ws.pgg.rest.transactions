package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    public List<User> findById(long id);

}
