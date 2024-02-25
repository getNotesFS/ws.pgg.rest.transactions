package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.GroupCategory;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.GroupRepository;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.UserGroupRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user-groups")
public class UserGroupsController {

    private final UserGroupRepository userGroupRepository;
    private  final GroupRepository groupRepository;

    public UserGroupsController(UserGroupRepository userGroupRepository, GroupRepository groupRepository ) {
        this.userGroupRepository = userGroupRepository;
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public ResponseEntity<?> getUserGroups() {
        return ResponseEntity.ok(userGroupRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserGroup(@PathVariable Long id) {
        List<UserGroup> found = userGroupRepository.
                findAll().stream().filter(f->f.getGroupCategory().getIdGroupCategory()==id)
                .collect(Collectors.toList());

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found);
        }
    }

    @PostMapping("/{groupId}/users")
    public ResponseEntity<List<URI>> assignUsersToGroup(@PathVariable Long groupId, @RequestBody List<Long> userIds) {
        // Verificar si el grupo existe
        GroupCategory group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        List<URI> createdUserGroupUris = new ArrayList<>();
        for (Long userId : userIds) {

            UserGroup userGroup = new UserGroup();
            userGroup.setGroupCategory(group);
            userGroup.setUserId(userId);
            userGroupRepository.save(userGroup);
            URI userGroupUri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userGroup.getUserId())
                    .toUri();
            createdUserGroupUris.add(userGroupUri);
        }
        if(!createdUserGroupUris.isEmpty())
            return ResponseEntity.created(createdUserGroupUris.get(0)).body(createdUserGroupUris);
       else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{userId}/users")
    public ResponseEntity<?> getAllGroupOfUser(@PathVariable Long userId){
        List<UserGroup> found = userGroupRepository.
                findAll().stream().filter(f->f.getUserId()==userId)
                .collect(Collectors.toList());

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found);
        }
    }

}
