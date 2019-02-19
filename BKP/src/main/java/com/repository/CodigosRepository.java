package com.repository;

import com.domain.Codigos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Codigos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodigosRepository extends JpaRepository<Codigos, Long>, JpaSpecificationExecutor<Codigos> {

}
