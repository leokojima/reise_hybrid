package com.reise.reise.repository;

import com.reise.reise.domain.Viajante;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Viajante entity.
 */
@Repository
public interface ViajanteRepository extends JpaRepository<Viajante, Long> {
    @Query(
        value = "select distinct viajante from Viajante viajante left join fetch viajante.locals left join fetch viajante.roteiros",
        countQuery = "select count(distinct viajante) from Viajante viajante"
    )
    Page<Viajante> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct viajante from Viajante viajante left join fetch viajante.locals left join fetch viajante.roteiros")
    List<Viajante> findAllWithEagerRelationships();

    @Query(
        "select viajante from Viajante viajante left join fetch viajante.locals left join fetch viajante.roteiros where viajante.id =:id"
    )
    Optional<Viajante> findOneWithEagerRelationships(@Param("id") Long id);
}
