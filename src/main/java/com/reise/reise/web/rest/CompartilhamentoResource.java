package com.reise.reise.web.rest;

import com.reise.reise.domain.Compartilhamento;
import com.reise.reise.repository.CompartilhamentoRepository;
import com.reise.reise.service.CompartilhamentoService;
import com.reise.reise.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reise.reise.domain.Compartilhamento}.
 */
@RestController
@RequestMapping("/api")
public class CompartilhamentoResource {

    private final Logger log = LoggerFactory.getLogger(CompartilhamentoResource.class);

    private static final String ENTITY_NAME = "compartilhamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompartilhamentoService compartilhamentoService;

    private final CompartilhamentoRepository compartilhamentoRepository;

    public CompartilhamentoResource(
        CompartilhamentoService compartilhamentoService,
        CompartilhamentoRepository compartilhamentoRepository
    ) {
        this.compartilhamentoService = compartilhamentoService;
        this.compartilhamentoRepository = compartilhamentoRepository;
    }

    /**
     * {@code POST  /compartilhamentos} : Create a new compartilhamento.
     *
     * @param compartilhamento the compartilhamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compartilhamento, or with status {@code 400 (Bad Request)} if the compartilhamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compartilhamentos")
    public ResponseEntity<Compartilhamento> createCompartilhamento(@Valid @RequestBody Compartilhamento compartilhamento)
        throws URISyntaxException {
        log.debug("REST request to save Compartilhamento : {}", compartilhamento);
        if (compartilhamento.getId() != null) {
            throw new BadRequestAlertException("A new compartilhamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Compartilhamento result = compartilhamentoService.save(compartilhamento);
        return ResponseEntity
            .created(new URI("/api/compartilhamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compartilhamentos/:id} : Updates an existing compartilhamento.
     *
     * @param id the id of the compartilhamento to save.
     * @param compartilhamento the compartilhamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compartilhamento,
     * or with status {@code 400 (Bad Request)} if the compartilhamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compartilhamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compartilhamentos/{id}")
    public ResponseEntity<Compartilhamento> updateCompartilhamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Compartilhamento compartilhamento
    ) throws URISyntaxException {
        log.debug("REST request to update Compartilhamento : {}, {}", id, compartilhamento);
        if (compartilhamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compartilhamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compartilhamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Compartilhamento result = compartilhamentoService.save(compartilhamento);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compartilhamento.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compartilhamentos/:id} : Partial updates given fields of an existing compartilhamento, field will ignore if it is null
     *
     * @param id the id of the compartilhamento to save.
     * @param compartilhamento the compartilhamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compartilhamento,
     * or with status {@code 400 (Bad Request)} if the compartilhamento is not valid,
     * or with status {@code 404 (Not Found)} if the compartilhamento is not found,
     * or with status {@code 500 (Internal Server Error)} if the compartilhamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compartilhamentos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Compartilhamento> partialUpdateCompartilhamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Compartilhamento compartilhamento
    ) throws URISyntaxException {
        log.debug("REST request to partial update Compartilhamento partially : {}, {}", id, compartilhamento);
        if (compartilhamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compartilhamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compartilhamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Compartilhamento> result = compartilhamentoService.partialUpdate(compartilhamento);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compartilhamento.getId().toString())
        );
    }

    /**
     * {@code GET  /compartilhamentos} : get all the compartilhamentos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compartilhamentos in body.
     */
    @GetMapping("/compartilhamentos")
    public List<Compartilhamento> getAllCompartilhamentos() {
        log.debug("REST request to get all Compartilhamentos");
        return compartilhamentoService.findAll();
    }

    /**
     * {@code GET  /compartilhamentos/:id} : get the "id" compartilhamento.
     *
     * @param id the id of the compartilhamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compartilhamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compartilhamentos/{id}")
    public ResponseEntity<Compartilhamento> getCompartilhamento(@PathVariable Long id) {
        log.debug("REST request to get Compartilhamento : {}", id);
        Optional<Compartilhamento> compartilhamento = compartilhamentoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compartilhamento);
    }

    /**
     * {@code DELETE  /compartilhamentos/:id} : delete the "id" compartilhamento.
     *
     * @param id the id of the compartilhamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compartilhamentos/{id}")
    public ResponseEntity<Void> deleteCompartilhamento(@PathVariable Long id) {
        log.debug("REST request to delete Compartilhamento : {}", id);
        compartilhamentoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
