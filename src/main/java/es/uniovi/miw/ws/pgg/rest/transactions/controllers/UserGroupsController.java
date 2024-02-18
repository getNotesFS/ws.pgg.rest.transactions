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
import java.util.ArrayList;
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


    @PostMapping("/{groupId}/users")
    public ResponseEntity<List<URI>> assignUsersToGroup(@PathVariable Long groupId, @RequestBody List<Long> userIds) {
        // Verificar si el grupo existe
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        System.out.println("Group: " + group);
        System.out.println("UserIds: " + userIds);

        List<URI> createdUserGroupUris = new ArrayList<>();

        // Iterar sobre los IDs de usuario y asignarlos al grupo
        for (Long userId : userIds) {
            // Verificar si la relación ya existe
            if (userGroupRepository.existsByUser_IdAndGroup_Id(userId, groupId)) {
                // Si la relación ya existe, ignorarla y pasar al siguiente usuario
                System.out.println("UserGroup already exists for userId: " + userId + " and groupId: " + groupId);
                continue;
            }

            // Verificar si el usuario existe
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Crear una nueva relación UserGroup
            UserGroup userGroup = new UserGroup();
            userGroup.setGroup(group);
            userGroup.setUser(user);

            System.out.println("\n=== NEW UserGroup: " + userGroup);

            // Guardar la nueva relación en la base de datos
            userGroupRepository.save(userGroup);
            // Construir la URI de la respuesta para este UserGroup
            URI userGroupUri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(userGroup.getId())
                    .toUri();

            createdUserGroupUris.add(userGroupUri);
        }
        if(!createdUserGroupUris.isEmpty()){
            return ResponseEntity.created(createdUserGroupUris.get(0)).body(createdUserGroupUris);
        } else {

            return ResponseEntity.notFound().build();
        }
    }




    @DeleteMapping("/{groupId}/users")
    public ResponseEntity<Void> removeUsersFromGroup(@PathVariable Long groupId, @RequestBody List<Long> userIds) {
        // Verificar si el grupo existe
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));
        List<URI> createdUserGroupUris = new ArrayList<>();
        // Iterar sobre los IDs de usuario y eliminar las relaciones UserGroup correspondientes
        for (Long userId : userIds) {
            // Verificar si el usuario existe
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Buscar la relación UserGroup existente
            UserGroup userGroup = userGroupRepository.findByGroupAndUser(group, user);

            System.out.println("Removing UserGroup relation: " + userGroup);

            // Verificar si la relación existe antes de intentar eliminarla
            if (userGroup != null) {

                URI userGroupUri = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(userGroup.getId())
                        .toUri();

                createdUserGroupUris.add(userGroupUri);

                userGroupRepository.delete(userGroup);
                userGroupRepository.flush();
            }else{
                return ResponseEntity.notFound().build();
            }
        }

        if(!createdUserGroupUris.isEmpty()){

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/ug-ids")
    public ResponseEntity<List<Long>> getUserGroupIdsByGroupIdAndUserId(
            @RequestParam Long groupId,
            @RequestParam Long userId) {
        List<Long> userGroupIds = userGroupRepository.findUserGroupIdsByGroupIdAndUserId(groupId, userId);
        System.out.println("UserGroup IDs: " + userGroupIds);
        return ResponseEntity.ok(userGroupIds);
    }

    @PostMapping
    public ResponseEntity<?> postUserGroup(@Valid @RequestBody UserGroup userGroup) {

        System.out.println("UserGroup: " + userGroup);



//        // Encontrar el grupo por su ID
//        Optional<Group> foundGroup = groupRepository.findById(userGroup.getGroupId());
//
//        // Verificar si se encontró el grupo
//        if (foundGroup.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Encontrar el usuario por su ID
//        Optional<User> foundUser = userRepository.findById(userGroup.getUserId());
//
//        // Verificar si se encontró el usuario
//        if (foundUser.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Asignar el grupo y el usuario al UserGroup
//        userGroup.setGroupId(foundGroup.get().getId());
//        userGroup.setUserId(foundUser.get().getId());


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
