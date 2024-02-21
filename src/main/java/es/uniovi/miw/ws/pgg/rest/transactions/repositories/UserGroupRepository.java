package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long>{

    //public List<UserGroup> findUserGroupByUserId(Long id);
    //@Query("SELECT COUNT(u) FROM UserGroup u WHERE u.groupCategory = (SELECT ug.groupCategory FROM UserGroup ug WHERE ug.userId = :userId) AND u.userId != :userId")
    //public int countUsersInSameGroup(Long userId);

    @Query("SELECT u FROM UserGroup u WHERE u.userId = :userId AND u.groupCategory.idGroupCategory = :groupCategoryId")
    public Optional<UserGroup> findUserGroupByUserIdAndGroupCategory(Long userId, Long groupCategoryId);

    @Query("SELECT COUNT(u) FROM UserGroup u WHERE u.groupCategory.idGroupCategory = :groupCategoryId")
    int countByGroupCategoryId(Long groupCategoryId);
}
