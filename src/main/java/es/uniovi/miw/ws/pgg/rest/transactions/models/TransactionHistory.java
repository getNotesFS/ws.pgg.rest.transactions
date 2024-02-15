package es.uniovi.miw.ws.pgg.rest.transactions.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.Date;
@Entity
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Min(0)
    private double debtAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRefund;

//    private long transaction_id;
//    private long userDebtor_id;
//    private long userGroup_id;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
    @ManyToOne
    @JoinColumn(name = "user_debtor_id")
    private User userDebtor;
    @ManyToOne
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    public TransactionHistory() {
    }

    public TransactionHistory(double debtAmount, Date dateRefund, Transaction transaction, User userDebtor, UserGroup userGroup) {
        this.debtAmount = debtAmount;
        this.dateRefund = dateRefund;
        this.transaction = transaction;
        this.userDebtor = userDebtor;
        this.userGroup = userGroup;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public User getUserDebtor() {
        return userDebtor;
    }

    public void setUserDebtor(User userDebtor) {
        this.userDebtor = userDebtor;
    }

    public double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(double debtAmount) {
        this.debtAmount = debtAmount;
    }

    public Date getDateRefund() {
        return dateRefund;
    }

    public void setDateRefund(Date dateRefund) {
        this.dateRefund = dateRefund;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }


//    public long getTransaction_id() {
//        return transaction_id;
//    }
//
//    public void setTransaction_id(long transaction_id) {
//        this.transaction_id = transaction_id;
//    }
//
//    public long getUserDebtor_id() {
//        return userDebtor_id;
//    }
//
//    public void setUserDebtor_id(long userDebtor_id) {
//        this.userDebtor_id = userDebtor_id;
//    }
//
//    public long getUserGroup_id() {
//        return userGroup_id;
//    }
//
//    public void setUserGroup_id(long userGroup_id) {
//        this.userGroup_id = userGroup_id;
//    }

    @Override
    public String toString() {
        return "TransactionHistory{" +
                "id=" + id +
                ", transaction=" + transaction +
                ", userDebtor=" + userDebtor +
                ", debtAmount=" + debtAmount +
                ", dateRefund=" + dateRefund +
                ", userGroup=" + userGroup +
                '}';
    }
}