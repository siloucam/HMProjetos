package com.repository;

import com.domain.Servico;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Servico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long>, JpaSpecificationExecutor<Servico> {
List<Servico> findByClienteId(Long Id);
}
