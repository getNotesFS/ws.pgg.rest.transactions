package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Group;
import es.uniovi.miw.ws.pgg.rest.transactions.models.User;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.GroupRepository;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.UserGroupRepository;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user-groups")
public class UserGroupsController {

    private final UserGroupRepository userGroupRepository;
    private  final GroupRepository groupRepository;

    private final UserRepository userRepository;

    public UserGroupsController(UserGroupRepository userGroupRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.userGroupRepository = userGroupRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }



    @GetMapping
    public ResponseEntity<?> getUserGroups() {
        return ResponseEntity.ok(userGroupRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserGroup(@PathVariable Long id) {
        Optional<UserGroup> found = userGroupRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found.get());
        }
    }

//    @PostMapping
//    public ResponseEntity<?> postUserGroup2(@Valid @RequestBody UserGroup userGroup) {
//
//        //Find the user group by the user and the group
//        Optional<Group> foundGroup = groupRepository.findById(userGroup.getGroup().getId());
//        List<User> foundUser = userRepository.findById(userGroup.getUser().getId());
//
//        if (foundGroup.isEmpty() || foundUser.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        userGroup.setGroup(foundGroup.get());
//        userGroup.setUser(foundUser.get(0));
//
//        userGroupRepository.saveAndFlush(userGroup);
//
//
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(userGroup.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).body(userGroup);
//    }

    @PostMapping
    public ResponseEntity<?> postUserGroup(@Valid @RequestBody UserGroup userGroup) {

        System.out.println("UserGroup: " + userGroup);
        // Verificar si el grupo está presente en el UserGroup
        if (userGroup.getGroup() == null) {
            return ResponseEntity.badRequest().body("Group is required");
        }

        // Encontrar el grupo por su ID
        Optional<Group> foundGroup = groupRepository.findById(userGroup.getGroup().getId());

        // Verificar si se encontró el grupo
        if (foundGroup.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Encontrar el usuario por su ID
        List<User> foundUser = userRepository.findById(userGroup.getUser().getId());

        // Verificar si se encontró el usuario
        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Asignar el grupo y el usuario al UserGroup
        userGroup.setGroup(foundGroup.get());
        userGroup.setUser(foundUser.get(0));

        // Guardar el UserGroup
        userGroupRepository.saveAndFlush(userGroup);

        // Construir la URI de la respuesta
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userGroup.getId())
                .toUri();

        // Devolver la respuesta con el código 201 (Created) y la URI de ubicación
        return ResponseEntity.created(location).body(userGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUserGroup(@PathVariable Long id, @Valid @RequestBody UserGroup userGroup) {
        Optional<UserGroup> found = userGroupRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            UserGroup current = found.get();
            current.setName(userGroup.getName());
            current.setTotalExpected(userGroup.getTotalExpected());
            current.setTotalContributed(userGroup.getTotalContributed());
            //current.setGroupG(userGroup.getGroupG());
            current.setUser(userGroup.getUser());
            // Add more fields if needed

            userGroupRepository.saveAndFlush(current);

            return ResponseEntity.ok(current);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserGroup(@PathVariable Long id) {
        Optional<UserGroup> found = userGroupRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            UserGroup current = found.get();
            userGroupRepository.delete(current);
            userGroupRepository.flush();
            return ResponseEntity.ok(current);
        }
    }
}
