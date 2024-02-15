package es.uniovi.miw.ws.pgg.rest.transactions.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groupG")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "group")
    @JsonIgnoreProperties("group")
    private List<UserGroup> userGroups = new ArrayList<>();

    public Group() {
    }
    public Group(String name) {
        this.name = name;
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

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }


    @Override
    public String toString() {
        return "GroupG{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userGroups=" + userGroups +
                '}';
    }
}