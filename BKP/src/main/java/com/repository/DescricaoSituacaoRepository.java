package com.repository;

import com.domain.DescricaoSituacao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DescricaoSituacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescricaoSituacaoRepository extends JpaRepository<DescricaoSituacao, Long>, JpaSpecificationExecutor<DescricaoSituacao> {

}
