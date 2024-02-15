package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.Transaction;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.TransactionRepository;
import jakarta.validation.Valid;
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

    public TransactionsController(TransactionRepository transactionRepository) {
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

        transaction.setDateExpense(new Date()); // Assuming current date/time for new transactions
        transactionRepository.saveAndFlush(transaction);

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
            current.setExpenses(transaction.getExpenses());
            current.setUser(transaction.getUser());
            current.setUserGroup(transaction.getUserGroup());
            // Add more fields if needed

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
