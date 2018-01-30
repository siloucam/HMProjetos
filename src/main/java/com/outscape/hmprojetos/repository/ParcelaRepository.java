package com.outscape.hmprojetos.repository;

import com.outscape.hmprojetos.domain.Parcela;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;


/**
 * Spring Data JPA repository for the Parcela entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
	List<Parcela> findByOrcamentoId(Long Id);
}
