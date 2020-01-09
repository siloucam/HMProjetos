package com.repository;

import com.domain.Terceiro;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Terceiro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TerceiroRepository extends JpaRepository<Terceiro, Long>, JpaSpecificationExecutor<Terceiro> {

}
