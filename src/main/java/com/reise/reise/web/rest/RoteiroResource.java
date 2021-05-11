package com.reise.reise.web.rest;

import com.reise.reise.domain.Roteiro;
import com.reise.reise.repository.RoteiroRepository;
import com.reise.reise.service.RoteiroService;
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
 * REST controller for managing {@link com.reise.reise.domain.Roteiro}.
 */
@RestController
@RequestMapping("/api")
public class RoteiroResource {

    private final Logger log = LoggerFactory.getLogger(RoteiroResource.class);

    private static final String ENTITY_NAME = "roteiro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoteiroService roteiroService;

    private final RoteiroRepository roteiroRepository;

    public RoteiroResource(RoteiroService roteiroService, RoteiroRepository roteiroRepository) {
        this.roteiroService = roteiroService;
        this.roteiroRepository = roteiroRepository;
    }

    /**
     * {@code POST  /roteiros} : Create a new roteiro.
     *
     * @param roteiro the roteiro to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roteiro, or with status {@code 400 (Bad Request)} if the roteiro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roteiros")
    public ResponseEntity<Roteiro> createRoteiro(@Valid @RequestBody Roteiro roteiro) throws URISyntaxException {
        log.debug("REST request to save Roteiro : {}", roteiro);
        if (roteiro.getId() != null) {
            throw new BadRequestAlertException("A new roteiro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Roteiro result = roteiroService.save(roteiro);
        return ResponseEntity
            .created(new URI("/api/roteiros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roteiros/:id} : Updates an existing roteiro.
     *
     * @param id the id of the roteiro to save.
     * @param roteiro the roteiro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roteiro,
     * or with status {@code 400 (Bad Request)} if the roteiro is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roteiro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/roteiros/{id}")
    public ResponseEntity<Roteiro> updateRoteiro(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Roteiro roteiro
    ) throws URISyntaxException {
        log.debug("REST request to update Roteiro : {}, {}", id, roteiro);
        if (roteiro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roteiro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roteiroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Roteiro result = roteiroService.save(roteiro);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roteiro.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roteiros/:id} : Partial updates given fields of an existing roteiro, field will ignore if it is null
     *
     * @param id the id of the roteiro to save.
     * @param roteiro the roteiro to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roteiro,
     * or with status {@code 400 (Bad Request)} if the roteiro is not valid,
     * or with status {@code 404 (Not Found)} if the roteiro is not found,
     * or with status {@code 500 (Internal Server Error)} if the roteiro couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/roteiros/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Roteiro> partialUpdateRoteiro(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Roteiro roteiro
    ) throws URISyntaxException {
        log.debug("REST request to partial update Roteiro partially : {}, {}", id, roteiro);
        if (roteiro.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roteiro.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roteiroRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Roteiro> result = roteiroService.partialUpdate(roteiro);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roteiro.getId().toString())
        );
    }

    /**
     * {@code GET  /roteiros} : get all the roteiros.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roteiros in body.
     */
    @GetMapping("/roteiros")
    public ResponseEntity<List<Roteiro>> getAllRoteiros(Pageable pageable) {
        log.debug("REST request to get a page of Roteiros");
        Page<Roteiro> page = roteiroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /roteiros/:id} : get the "id" roteiro.
     *
     * @param id the id of the roteiro to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roteiro, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/roteiros/{id}")
    public ResponseEntity<Roteiro> getRoteiro(@PathVariable Long id) {
        log.debug("REST request to get Roteiro : {}", id);
        Optional<Roteiro> roteiro = roteiroService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roteiro);
    }

    /**
     * {@code DELETE  /roteiros/:id} : delete the "id" roteiro.
     *
     * @param id the id of the roteiro to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roteiros/{id}")
    public ResponseEntity<Void> deleteRoteiro(@PathVariable Long id) {
        log.debug("REST request to delete Roteiro : {}", id);
        roteiroService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
