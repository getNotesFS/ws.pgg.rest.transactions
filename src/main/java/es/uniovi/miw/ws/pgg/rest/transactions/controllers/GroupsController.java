package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.*;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins= "*", maxAge = 3600)
@RestController

@RequestMapping("/api/groups")
public class GroupsController {


    private final GroupRepository groupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private DetailsRepository detailsRepository;

    public GroupsController(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public ResponseEntity<?> getGroups(
            @RequestParam(value = "name", required = false) String name) {
        // Filter
        if(name != null) {
            return ResponseEntity.ok(groupRepository.findByName(name));
        }

        return ResponseEntity.ok(groupRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable Long id) {
        Optional<Group>  found = groupRepository.findById(id);

        if(found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found.get());
        }
    }

    @PostMapping
    public ResponseEntity<?> postGroup(@Valid @RequestBody Group group){



        groupRepository.saveAndFlush(group);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(group.getId())
                .toUri();

        return ResponseEntity.created(location).body(group);

    }


    @GetMapping("/{groupId}/history")
    public ResponseEntity<List<History>> getGroupTransactionHistory(@PathVariable Long groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Group group = groupOptional.get();
        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);
        List<History> groupTransactionHistory = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            List<History> userGroupHistory = historyRepository.findByUserGroup(userGroup);
            groupTransactionHistory.addAll(userGroupHistory);
        }
        return ResponseEntity.ok(groupTransactionHistory);
    }

    @GetMapping("/{groupId}/users/{userId}/transactions/{transactionId}/details")
    public ResponseEntity<Details> getTransactionDetails(@PathVariable Long groupId,
                                                         @PathVariable Long userId,
                                                         @PathVariable Long transactionId) {
        UserGroup userGroup = userGroupRepository.findByUser_IdAndGroup_Id(userId, groupId);
        if (userGroup == null) {
            return ResponseEntity.notFound().build();
        }
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Transaction transaction = transactionOptional.get();
        Details details = detailsRepository.findByTransactionAndUserGroup(transaction, userGroup);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> putGroup(@PathVariable long id, @Valid @RequestBody Group group){

        System.out.println("putGroup: " + id + " " + group);

        Optional<Group> found = groupRepository.findById(id);

        if(found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Group current = found.get();
            current.setName(group.getName());
            current.setId(group.getId());
            //current.setUsers(group.getUsers());
            groupRepository.saveAndFlush(current);

            return ResponseEntity.ok(current);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id){
        Optional<Group> found = groupRepository.findById(id);

        if(found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Group current = found.get();
            groupRepository.delete(current);
            groupRepository.flush();
            return ResponseEntity.ok(current);

        }
    }


}
