package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Group;
import es.uniovi.miw.ws.pgg.rest.transactions.models.User;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.GroupRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins= "*", maxAge = 3600)
@RestController

@RequestMapping("/api/groups")
public class GroupsController {


    private final GroupRepository groupRepository;

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
            if(group.getMasterOfGroupId() != 0){
                current.setMasterOfGroupId(group.getMasterOfGroupId());
            }
            if(group.getTotalContributed() != 0){
                current.setTotalContributed(group.getTotalContributed());
            }
            if(group.getUsers() != null){
                current.setUsers(group.getUsers());
            }
            System.out.println("putGroup: " + current);

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
