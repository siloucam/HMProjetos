package com.repository;

import com.domain.MeuServico;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MeuServico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MeuServicoRepository extends JpaRepository<MeuServico, Long>, JpaSpecificationExecutor<MeuServico> {

}
