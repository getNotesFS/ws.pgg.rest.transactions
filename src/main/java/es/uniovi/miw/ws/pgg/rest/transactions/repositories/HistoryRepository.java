package es.uniovi.miw.ws.pgg.rest.transactions.repositories;

import es.uniovi.miw.ws.pgg.rest.transactions.models.History;
import es.uniovi.miw.ws.pgg.rest.transactions.models.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {




}