package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.*;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionRepository transactionRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private DetailsRepository detailsRepository;

    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;



    public TransactionsController(TransactionRepository transactionRepository ) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public ResponseEntity<?> getTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id) {
        Optional<Transaction> found = transactionRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found.get());
        }
    }


    @PostMapping
    public ResponseEntity<?> postTransaction(@Valid @RequestBody Transaction transaction,
                                             @RequestParam("idGroup") Long idGroup,
                                             @RequestParam("idUser") Long idUser){


        System.out.println("Transaction: " + transaction);
        System.out.println("idGroup: " + idGroup);
        System.out.println("idUser: " + idUser);

        transaction.setDateExpense(new Date());

        transactionRepository.saveAndFlush(transaction);

        Optional<Group> foundGroup = groupRepository.findById(idGroup);
        if (foundGroup.isEmpty()) {
            System.out.println("Group not found");
            return ResponseEntity.notFound().build();
        }else{
            System.out.println("Found Group: " + foundGroup.get());
        }

        // Update group totalContributed
        Optional<User> foundUser = userRepository.findById(idUser);
        
        if (foundUser.isEmpty()) {
            System.out.println("User not found");
            return ResponseEntity.notFound().build();
        }else{
            System.out.println("Found User: " + foundUser.get());
            foundUser.ifPresent(user -> {
                user.setTotalAmount(user.getTotalAmount() + transaction.getExpense());
                userRepository.saveAndFlush(user);
            });
        }
        
        
        UserGroup foundUserGroup = userGroupRepository.findByUser_IdAndGroup_Id(idUser, idGroup);
        System.out.println("Found UserGroup: " + foundUserGroup);

        if (foundUserGroup == null) {
            System.out.println("UserGroup not found");
            return ResponseEntity.notFound().build();
        }else{
            System.out.println("Found UserGroup: " + foundUserGroup);
            foundUserGroup.setTotalIndividual(foundUserGroup.getUser().getTotalAmount() / foundUserGroup.getGroup().getUsers().size()); 
            userGroupRepository.saveAndFlush(foundUserGroup);

        }

        // Add details relation

        Details details = new Details();
        details.setTransaction(transaction);
        details.setUserGroup(foundUserGroup);
        detailsRepository.saveAndFlush(details);

        // Add history relation
        History history = new History();
        history.setDetails(details);
        history.setUserGroup(foundUserGroup);
        history.setTotal(transaction.getExpense() / foundUserGroup.getGroup().getUsers().size());
        historyRepository.saveAndFlush(history);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transaction.getId())
                .toUri();

        return ResponseEntity.created(location).body(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putTransaction(@PathVariable Long id, @Valid @RequestBody Transaction transaction) {
        Optional<Transaction> found = transactionRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Transaction current = found.get();
            current.setDescription(transaction.getDescription());
            current.setDateExpense(new Date());
            current.setExpense(transaction.getExpense());


            transactionRepository.saveAndFlush(current);


            return ResponseEntity.ok(current);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        Optional<Transaction> found = transactionRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Transaction current = found.get();
            transactionRepository.delete(current);
            transactionRepository.flush();
            return ResponseEntity.ok(current);
        }
    }
}
