package com.reise.reise.repository;

import com.reise.reise.domain.Compartilhamento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Compartilhamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompartilhamentoRepository extends JpaRepository<Compartilhamento, Long> {}
