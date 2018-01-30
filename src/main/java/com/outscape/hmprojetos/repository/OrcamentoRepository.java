package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.Orcamento;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Orcamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
	List<Orcamento> findByServicoId(Long Id);
}
