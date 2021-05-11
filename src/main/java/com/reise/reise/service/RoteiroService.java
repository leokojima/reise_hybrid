package com.reise.reise.service;

import com.reise.reise.domain.Roteiro;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Roteiro}.
 */
public interface RoteiroService {
    /**
     * Save a roteiro.
     *
     * @param roteiro the entity to save.
     * @return the persisted entity.
     */
    Roteiro save(Roteiro roteiro);

    /**
     * Partially updates a roteiro.
     *
     * @param roteiro the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Roteiro> partialUpdate(Roteiro roteiro);

    /**
     * Get all the roteiros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Roteiro> findAll(Pageable pageable);

    /**
     * Get the "id" roteiro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Roteiro> findOne(Long id);

    /**
     * Delete the "id" roteiro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
