package com.reise.reise.web.rest;

import com.reise.reise.domain.Viajante;
import com.reise.reise.repository.ViajanteRepository;
import com.reise.reise.service.ViajanteService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.reise.reise.domain.Viajante}.
 */
@RestController
@RequestMapping("/api")
public class ViajanteResource {

    private final Logger log = LoggerFactory.getLogger(ViajanteResource.class);

    private static final String ENTITY_NAME = "viajante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViajanteService viajanteService;

    private final ViajanteRepository viajanteRepository;

    public ViajanteResource(ViajanteService viajanteService, ViajanteRepository viajanteRepository) {
        this.viajanteService = viajanteService;
        this.viajanteRepository = viajanteRepository;
    }

    /**
     * {@code POST  /viajantes} : Create a new viajante.
     *
     * @param viajante the viajante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viajante, or with status {@code 400 (Bad Request)} if the viajante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/viajantes")
    public ResponseEntity<Viajante> createViajante(@Valid @RequestBody Viajante viajante) throws URISyntaxException {
        log.debug("REST request to save Viajante : {}", viajante);
        if (viajante.getId() != null) {
            throw new BadRequestAlertException("A new viajante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Viajante result = viajanteService.save(viajante);
        return ResponseEntity
            .created(new URI("/api/viajantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /viajantes/:id} : Updates an existing viajante.
     *
     * @param id the id of the viajante to save.
     * @param viajante the viajante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viajante,
     * or with status {@code 400 (Bad Request)} if the viajante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viajante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/viajantes/{id}")
    public ResponseEntity<Viajante> updateViajante(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Viajante viajante
    ) throws URISyntaxException {
        log.debug("REST request to update Viajante : {}, {}", id, viajante);
        if (viajante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viajante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viajanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Viajante result = viajanteService.save(viajante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viajante.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /viajantes/:id} : Partial updates given fields of an existing viajante, field will ignore if it is null
     *
     * @param id the id of the viajante to save.
     * @param viajante the viajante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viajante,
     * or with status {@code 400 (Bad Request)} if the viajante is not valid,
     * or with status {@code 404 (Not Found)} if the viajante is not found,
     * or with status {@code 500 (Internal Server Error)} if the viajante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/viajantes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Viajante> partialUpdateViajante(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Viajante viajante
    ) throws URISyntaxException {
        log.debug("REST request to partial update Viajante partially : {}, {}", id, viajante);
        if (viajante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viajante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viajanteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Viajante> result = viajanteService.partialUpdate(viajante);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, viajante.getId().toString())
        );
    }

    /**
     * {@code GET  /viajantes} : get all the viajantes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viajantes in body.
     */
    @GetMapping("/viajantes")
    public ResponseEntity<List<Viajante>> getAllViajantes(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Viajantes");
        Page<Viajante> page;
        if (eagerload) {
            page = viajanteService.findAllWithEagerRelationships(pageable);
        } else {
            page = viajanteService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /viajantes/:id} : get the "id" viajante.
     *
     * @param id the id of the viajante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viajante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/viajantes/{id}")
    public ResponseEntity<Viajante> getViajante(@PathVariable Long id) {
        log.debug("REST request to get Viajante : {}", id);
        Optional<Viajante> viajante = viajanteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viajante);
    }

    /**
     * {@code DELETE  /viajantes/:id} : delete the "id" viajante.
     *
     * @param id the id of the viajante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/viajantes/{id}")
    public ResponseEntity<Void> deleteViajante(@PathVariable Long id) {
        log.debug("REST request to delete Viajante : {}", id);
        viajanteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
