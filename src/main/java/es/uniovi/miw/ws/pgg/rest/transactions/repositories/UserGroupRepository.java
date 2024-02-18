package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Group;
import es.uniovi.miw.ws.pgg.rest.transactions.models.User;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long>{
    public List<UserGroup> findByUserId(Long userId);
    public List<UserGroup> findByGroupId(Long groupId);

    @Query("SELECT ug FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId")
    public List<UserGroup> findByUserIdAndGroupId(Long userId, Long groupId);

    @Query("SELECT ug FROM UserGroup ug WHERE ug.user.id = :userId AND ug.group.id = :groupId")
    public UserGroup findByUser_IdAndGroup_Id(Long userId, Long groupId);

    @Query("SELECT ug.id FROM UserGroup ug WHERE ug.group.id = :groupId AND ug.user.id = :userId")
    public List<Long> findUserGroupIdsByGroupIdAndUserId(Long groupId, Long userId);
    public  UserGroup findByGroupAndUser(Group group,User user);

    @Query("SELECT ug.group FROM UserGroup ug WHERE ug.user = :user")
    public List<Group> findGroupsByUser(@Param("user") User user);

   public boolean existsByUser_IdAndGroup_Id(Long userId, Long groupId);


}
