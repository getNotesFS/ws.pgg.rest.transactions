package es.uniovi.miw.ws.pgg.rest.transactions.models;

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
    private Long idDetail;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "group_user_id" )
    })
    private UserGroup userGroup;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Override
    public String toString()  {
        return "Details{" +
                "id=" + idDetail +
                ", idTransaction=" + transaction.getIdTransaction() +
                ", idUserGroup=" + userGroup.getGroupCategory() +
                '}';
    }
}
