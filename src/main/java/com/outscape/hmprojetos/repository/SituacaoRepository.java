package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.Situacao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Situacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SituacaoRepository extends JpaRepository<Situacao, Long> {
	List<Situacao> findByServicoId(Long Id);
}
