package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Group;
import es.uniovi.miw.ws.pgg.rest.transactions.models.User;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.GroupRepository;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.TransactionRepository;
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

    private final TransactionRepository transactionRepository;


    public UserGroupsController(UserGroupRepository userGroupRepository, GroupRepository groupRepository, UserRepository userRepository, TransactionRepository transactionRepository ) {
        this.userGroupRepository = userGroupRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
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



    @PostMapping
    public ResponseEntity<?> postUserGroup(@Valid @RequestBody UserGroup userGroup) {

        System.out.println("UserGroup: " + userGroup);

        UserGroup currentUserGroup = userGroupRepository.findById(userGroup.getId()).orElse(null);

        if (currentUserGroup != null) {
            return ResponseEntity.badRequest().build();
        }



        // Encontrar el grupo por su ID
        Optional<Group> foundGroup = groupRepository.findById(userGroup.getGroupId());

        // Verificar si se encontró el grupo
        if (foundGroup.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Encontrar el usuario por su ID
        List<User> foundUser = userRepository.findById(userGroup.getUserId());

        // Verificar si se encontró el usuario
        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Asignar el grupo y el usuario al UserGroup
        userGroup.setGroupId(foundGroup.get().getId());
        userGroup.setUserId(foundUser.get(0).getId());


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
            current.setUserId(userGroup.getUserId());
            current.setGroupId(userGroup.getGroupId());

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
