package es.uniovi.miw.ws.pgg.rest.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistory;

    @OneToOne
    private Details details;

    @Min(0)
    private double total;

    @Transient
    private double totalCost;

    @Transient
    private Date dateExpense;
    @Override
    public String toString()  { return "History{" +
            "id=" + idHistory +
            ", details=" + details +
            ", total=" + total +
            '}';
    }
}
