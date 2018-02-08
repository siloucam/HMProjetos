package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.Codigos;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Codigos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CodigosRepository extends JpaRepository<Codigos, Long> {

}
