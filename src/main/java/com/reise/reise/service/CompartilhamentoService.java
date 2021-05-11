package com.reise.reise.service;

import com.reise.reise.domain.Compartilhamento;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Compartilhamento}.
 */
public interface CompartilhamentoService {
    /**
     * Save a compartilhamento.
     *
     * @param compartilhamento the entity to save.
     * @return the persisted entity.
     */
    Compartilhamento save(Compartilhamento compartilhamento);

    /**
     * Partially updates a compartilhamento.
     *
     * @param compartilhamento the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Compartilhamento> partialUpdate(Compartilhamento compartilhamento);

    /**
     * Get all the compartilhamentos.
     *
     * @return the list of entities.
     */
    List<Compartilhamento> findAll();

    /**
     * Get the "id" compartilhamento.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Compartilhamento> findOne(Long id);

    /**
     * Delete the "id" compartilhamento.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
