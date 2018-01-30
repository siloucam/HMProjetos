package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.Transacao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Transacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
	List<Transacao> findByServicoId(Long Id);
}
