package com.reise.reise.repository;

import com.reise.reise.domain.Roteiro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Roteiro entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoteiroRepository extends JpaRepository<Roteiro, Long> {}
