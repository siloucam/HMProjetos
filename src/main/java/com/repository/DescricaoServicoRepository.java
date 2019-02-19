package com.repository;

import com.domain.DescricaoServico;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DescricaoServico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescricaoServicoRepository extends JpaRepository<DescricaoServico, Long>, JpaSpecificationExecutor<DescricaoServico> {

}
