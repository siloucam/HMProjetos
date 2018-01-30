package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.Telefone;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Telefone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
List<Telefone> findByClienteId(Long Id);
}
