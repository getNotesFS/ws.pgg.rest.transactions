package es.uniovi.miw.ws.pgg.rest.transactions.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id")
    private long userId;

    @Column(name = "group_id")
    private long groupId;

    @NotBlank
    private String description;

    @Min(0)
    private double graduation;

    @Min(0)
    private double entry;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateExpense;

    @Override
    public String toString() { return "Transaction{" + "id=" + id + ", userId=" + userId + ", groupId=" + groupId + ", description='" + description + '\'' + ", graduation=" + graduation + ", entry=" + entry + ", dateExpense=" + dateExpense + '}';
    }
}