package com.repository;

import com.domain.LinkExterno;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LinkExterno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LinkExternoRepository extends JpaRepository<LinkExterno, Long>, JpaSpecificationExecutor<LinkExterno> {

}
