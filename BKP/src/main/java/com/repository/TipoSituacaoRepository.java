package com.repository;

import com.domain.TipoSituacao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TipoSituacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoSituacaoRepository extends JpaRepository<TipoSituacao, Long>, JpaSpecificationExecutor<TipoSituacao> {

}
