package es.uniovi.miw.ws.pgg.rest.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String name;

    @Min(0)
    private double totalExpected;

    @Min(0)
    private double totalContributed;

//    private long group_id;
//    private long user_id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "userGroup")

    private List<TransactionHistory> transactionHistories = new ArrayList<>();

    public UserGroup() {
    }

    public UserGroup(String name, double totalExpected, double totalContributed, Group group, User user) {
        this.name = name;
        this.totalExpected = totalExpected;
        this.totalContributed = totalContributed;
        this.group = group;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalExpected() {
        return totalExpected;
    }

    public void setTotalExpected(double totalExpected) {
        this.totalExpected = totalExpected;
    }

    public double getTotalContributed() {
        return totalContributed;
    }

    public void setTotalContributed(double totalContributed) {
        this.totalContributed = totalContributed;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TransactionHistory> getTransactionHistories() {
        return transactionHistories;
    }

    public void setTransactionHistories(List<TransactionHistory> transactionHistories) {
        this.transactionHistories = transactionHistories;
    }

//    public long getGroup_id() {
//        return group_id;
//    }
//
//    public void setGroup_id(long group_id) {
//        this.group_id = group_id;
//    }
//
//    public long getUser_id() {
//        return user_id;
//    }
//
//    public void setUser_id(long user_id) {
//        this.user_id = user_id;
//    }

    @Override
    public String toString() {
        return "User_Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalExpected=" + totalExpected +
                ", totalContributed=" + totalContributed +
                ", group=" + group +
                ", user=" + user +
                ", transactionHistories=" + transactionHistories +
                '}';
    }
}