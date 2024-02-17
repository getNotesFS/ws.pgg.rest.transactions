package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Group;
import es.uniovi.miw.ws.pgg.rest.transactions.models.Transaction;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.GroupRepository;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    private final TransactionRepository transactionRepository;

    @Autowired
    private GroupRepository groupRepository;



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
    public ResponseEntity<?> postTransaction(@Valid @RequestBody Transaction transaction) {

        // check if the user is in the group
        Optional<Group> groupOptionalFoundUser = groupRepository.findById(transaction.getGroupId());
        if (groupOptionalFoundUser.isPresent()) {
            Group group = groupOptionalFoundUser.get();
            if (!group.getUsers().contains(transaction.getUserId())) {
                return ResponseEntity.badRequest().body("The user is not in the group");
            }
        } else {
            return ResponseEntity.badRequest().body("The group does not exist");
        }

        transaction.setDateExpense(new Date());
        transactionRepository.saveAndFlush(transaction);

        // Update group totalContributed
        Optional<Group> groupOptional = groupRepository.findById(transaction.getGroupId());
        if (groupOptional.isPresent()) {
            Group group = groupOptional.get();
            group.setTotalContributed(group.getTotalContributed() + transaction.getGraduation());
            groupRepository.saveAndFlush(group);
        }


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
            current.setUserId(transaction.getUserId());
            current.setGroupId(transaction.getGroupId());
            current.setGraduation(transaction.getGraduation());
            current.setEntry(transaction.getEntry());
            current.setDateExpense(new Date());


            transactionRepository.saveAndFlush(current);

            Optional<Group> groupOptional = groupRepository.findById(current.getGroupId());

            if (groupOptional.isPresent()) {
                Group group = groupOptional.get();
                group.setTotalContributed(group.getTotalContributed() + current.getGraduation());
                groupRepository.saveAndFlush(group);
            }



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
