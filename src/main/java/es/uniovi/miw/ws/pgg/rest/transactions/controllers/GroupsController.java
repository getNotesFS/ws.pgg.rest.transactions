package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.*;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
        Optional<GroupCategory> found = groupRepository.findById(id);

        if(found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found.get());
        }
    }
    @GetMapping("/unassigned")
    public ResponseEntity<?> unassignedGroup(
            @RequestParam(value = "name", required = false) String name) {

        List<GroupCategory> groupCategories = groupRepository.findAll()
                .stream().filter(f->f.getIdMaster()==null).toList();
        //if the Tolist the problem so using collect(Collectors.toList());
        return ResponseEntity.ok(groupCategories);
    }
    @PostMapping
    public ResponseEntity<?> postGroup(@Valid @RequestBody GroupCategory groupCategory){
        groupRepository.saveAndFlush(groupCategory);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(groupCategory.getIdGroupCategory())
                .toUri();

        return ResponseEntity.created(location).body(groupCategory);

    }




}
