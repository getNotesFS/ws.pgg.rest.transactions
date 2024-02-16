package es.uniovi.miw.ws.pgg.rest.transactions.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //private long user_id;
//    private long userGroup_id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    @ManyToOne
    @JoinColumn(name = "user_group_id")
    @JsonIgnore
    private UserGroup userGroup;

    @NotBlank
    private String description;
    @Min(0)
    private double expenses;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExpense;

    public Transaction() {
    }

    public Transaction(User user,UserGroup userGroup, String description, double expenses) {
        this.user = user;
        this.userGroup = userGroup;
        this.description = description;
        this.expenses = expenses;
        this.dateExpense = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public Date getDateExpense() {
        return dateExpense;
    }

    public void setDateExpense(Date dateExpense) {
        this.dateExpense = dateExpense;
    }

//    public long getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(long user_id) {
//        this.user_id = user_id;
//    }



//    public long getUserGroup_id() {
//        return userGroup_id;
//    }
//
//    public void setUserGroup_id(long userGroup_id) {
//        this.userGroup_id = userGroup_id;
//    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", user=" + user +
                ", userGroup=" + userGroup +
                ", description='" + description + '\'' +
                ", expenses=" + expenses +
                ", dateExpense=" + dateExpense +
                '}';
    }
}