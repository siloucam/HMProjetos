package com.repository;

import com.domain.Situacao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Situacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SituacaoRepository extends JpaRepository<Situacao, Long>, JpaSpecificationExecutor<Situacao> {
List<Situacao> findByServicoId(Long Id);
}
