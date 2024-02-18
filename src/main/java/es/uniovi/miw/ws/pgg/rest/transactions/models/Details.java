package es.uniovi.miw.ws.pgg.rest.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "group_id", referencedColumnName = "group_id"),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    })
    private UserGroup userGroup;




    @ManyToOne
    @JoinColumn(name = "transaction_id")
   // @JsonIgnore
    private Transaction transaction;

//    @ManyToOne
//    @JoinColumn(name = "history_id")
//    private History history;

    @Override
    public String toString()  {
        return "Details{" +
                "id=" + id +
                ", idTransaction=" + transaction.getId() +
                ", idUserGroup=" + userGroup.getId() +
                '}';
    }
}
