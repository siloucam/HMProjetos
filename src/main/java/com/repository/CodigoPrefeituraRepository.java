package com.repository;

import com.domain.CodigoPrefeitura;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CodigoPrefeitura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodigoPrefeituraRepository extends JpaRepository<CodigoPrefeitura, Long>, JpaSpecificationExecutor<CodigoPrefeitura> {

}
