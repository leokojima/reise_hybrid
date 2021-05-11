package com.reise.reise.service;

import com.reise.reise.domain.Viajante;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Viajante}.
 */
public interface ViajanteService {
    /**
     * Save a viajante.
     *
     * @param viajante the entity to save.
     * @return the persisted entity.
     */
    Viajante save(Viajante viajante);

    /**
     * Partially updates a viajante.
     *
     * @param viajante the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Viajante> partialUpdate(Viajante viajante);

    /**
     * Get all the viajantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Viajante> findAll(Pageable pageable);

    /**
     * Get all the viajantes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Viajante> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" viajante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Viajante> findOne(Long id);

    /**
     * Delete the "id" viajante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
