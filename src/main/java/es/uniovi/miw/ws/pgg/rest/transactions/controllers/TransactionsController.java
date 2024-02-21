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

    private  TransactionRepository transactionRepository;
    private UserGroupRepository userGroupRepository;

    private DetailsRepository detailsRepository;

    private HistoryRepository historyRepository;

    public TransactionsController(TransactionRepository transactionRepository,
                                  UserGroupRepository userGroupRepository,
                                  DetailsRepository detailsRepository,
                                  HistoryRepository historyRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.userGroupRepository = userGroupRepository;
        this.detailsRepository=detailsRepository;
        this.historyRepository=historyRepository;
    }

    @GetMapping
    public ResponseEntity<?> getTransactions() {
        return ResponseEntity.ok(transactionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id) {
        Optional<Transaction> found = transactionRepository.findByIdTransaction(id);

        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(found.get());
        }
    }


    @PostMapping
    public ResponseEntity<?> postTransaction(@Valid @RequestBody Transaction transaction,
                                             @RequestParam("idGroup") Long idGroup,
                                             @RequestParam("idUser") Long idUser
                                             ){


        System.out.println("Transaction: " + transaction);
        System.out.println("idGroup: " + idGroup);

        transaction.setDateExpense(new Date());

        transactionRepository.saveAndFlush(transaction);

        Optional<UserGroup> foundUserGroup = userGroupRepository.findUserGroupByUserIdAndGroupCategory(idUser,idGroup);
        if (foundUserGroup.isEmpty()) {
            System.out.println("Group not found");
            transactionRepository.delete(transaction);
            return ResponseEntity.notFound().build();
        }else{
            System.out.println("Found Group: " + foundUserGroup.get());
        }
        // Add details relation

        Details details = new Details();
        details.setTransaction(transaction);
        details.setUserGroup(foundUserGroup.get());
        detailsRepository.saveAndFlush(details);

        // Add history relation
        History history = new History();
        history.setDetails(details);
        history.setTotal(transaction.getExpense() / userGroupRepository.countByGroupCategoryId(foundUserGroup.get().getGroupCategory().getIdGroupCategory()));
        historyRepository.saveAndFlush(history);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transaction.getIdTransaction())
                .toUri();

        return ResponseEntity.created(location).body(transaction);
    }


}
