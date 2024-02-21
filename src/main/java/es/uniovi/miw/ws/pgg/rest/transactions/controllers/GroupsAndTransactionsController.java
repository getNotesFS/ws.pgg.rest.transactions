package es.uniovi.miw.ws.pgg.rest.transactions.controllers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import es.uniovi.miw.ws.pgg.rest.transactions.models.Transaction;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.DetailsRepository;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.TransactionRepository;
import es.uniovi.miw.ws.pgg.rest.transactions.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins= "*", maxAge = 3600)
@RestController

@RequestMapping("/api/groups/{groupId}/transactions")
public class GroupsAndTransactionsController {

    @Autowired
    private  TransactionRepository transactionRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private DetailsRepository detailsRepository;
    @GetMapping
    public ResponseEntity<?> getTransactionsByGroupId(@PathVariable Long groupId) {

        Optional<UserGroup> foundUserGroup = userGroupRepository.findById(groupId);
        List<Transaction> transactions =detailsRepository.findTransactionsByUserGroup(foundUserGroup.get());

        System.out.println("Transactions: " + transactions);
        if (!transactions.isEmpty()) {
            return ResponseEntity.ok(transactions);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
