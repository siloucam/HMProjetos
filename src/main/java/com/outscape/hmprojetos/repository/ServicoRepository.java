package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.Servico;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Servico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
	List<Servico> findByClienteId(Long Id);
}
