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
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Optional<UserGroup> foundUserGroup = userGroupRepository.findAll().stream().filter(f->f.getUserId()==idUser && f.getIdUserGroup()==idGroup).findFirst();
                //.findUserGroupByUserIdAndGroupCategory(idUser,idGroup);
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
        double equalParts=transaction.getExpense() / userGroupRepository.countByGroupCategoryId(foundUserGroup.get().getGroupCategory().getIdGroupCategory());
        DecimalFormat df = new DecimalFormat("#.##");
        history.setTotal(Double.parseDouble(df.format(equalParts)));
        historyRepository.saveAndFlush(history);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transaction.getIdTransaction())
                .toUri();

        return ResponseEntity.created(location).body(transaction);
    }

    @GetMapping("/{groupId}/history")
    public ResponseEntity<?> getGroupTransactionHistory(@PathVariable Long groupId) {
        Optional<UserGroup> groupOptional = userGroupRepository.findById(groupId);
        if (groupOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserGroup group = groupOptional.get();

        List<History> groupTransactionHistory = historyRepository.findAll().stream().filter(f->f.getDetails().getUserGroup().getIdUserGroup()==groupId).collect(Collectors.toList());
        if (groupTransactionHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        for (History h : groupTransactionHistory) {
            h.setTotalCost(h.getDetails().getTransaction().getExpense());
            h.setDateExpense(h.getDetails().getTransaction().getDateExpense());
            //int individualTotal= userGroupRepository.countByGroupCategoryId( h.getDetails().getUserGroup().getGroupCategory().getIdGroupCategory());
            //h.setTotal(h.getTotal()/individualTotal);
        }
        return ResponseEntity.ok(groupTransactionHistory);
    }
}
