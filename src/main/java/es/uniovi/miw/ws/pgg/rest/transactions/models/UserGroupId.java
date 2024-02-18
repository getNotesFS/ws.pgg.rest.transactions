package es.uniovi.miw.ws.pgg.rest.transactions.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupId implements Serializable {
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "user_id")
    private Long userId;

}
