package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import es.uniovi.miw.ws.pgg.rest.transactions.models.TransactionHistory;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.TransactionHistoryRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transaction-histories")
public class TransactionHistoriesController {

    private final TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoriesController(TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    @GetMapping
    public ResponseEntity<?> getTransactionHistories() {
        return ResponseEntity.ok(transactionHistoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long id) {
        Optional<TransactionHistory> found = transactionHistoryRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found.get());
        }
    }

    @PostMapping
    public ResponseEntity<?> postTransactionHistory(@Valid @RequestBody TransactionHistory transactionHistory) {

        transactionHistory.setDateRefund(new Date()); // Assuming current date/time for new transaction histories
        transactionHistoryRepository.saveAndFlush(transactionHistory);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transactionHistory.getId())
                .toUri();

        return ResponseEntity.created(location).body(transactionHistory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putTransactionHistory(@PathVariable Long id, @Valid @RequestBody TransactionHistory transactionHistory) {
        Optional<TransactionHistory> found = transactionHistoryRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            TransactionHistory current = found.get();
            current.setDebtAmount(transactionHistory.getDebtAmount());
            current.setDateRefund(transactionHistory.getDateRefund());
            current.setTransaction(transactionHistory.getTransaction());
            current.setUserDebtor(transactionHistory.getUserDebtor());
            current.setUserGroup(transactionHistory.getUserGroup());
            // Add more fields if needed

            transactionHistoryRepository.saveAndFlush(current);

            return ResponseEntity.ok(current);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransactionHistory(@PathVariable Long id) {
        Optional<TransactionHistory> found = transactionHistoryRepository.findById(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            TransactionHistory current = found.get();
            transactionHistoryRepository.delete(current);
            transactionHistoryRepository.flush();
            return ResponseEntity.ok(current);
        }
    }
}
