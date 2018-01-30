package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.ExtendUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ExtendUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExtendUserRepository extends JpaRepository<ExtendUser, Long> {
	List<ExtendUser> findByUserId(Long Id);
}
