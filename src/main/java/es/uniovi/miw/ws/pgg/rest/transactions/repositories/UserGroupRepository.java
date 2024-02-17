package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long>{
    public List<UserGroup> findByUserId(Long userId);

}
