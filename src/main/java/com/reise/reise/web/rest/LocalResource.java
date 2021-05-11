package com.reise.reise.web.rest;

import com.reise.reise.domain.Local;
import com.reise.reise.repository.LocalRepository;
import com.reise.reise.service.LocalService;
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
 * REST controller for managing {@link com.reise.reise.domain.Local}.
 */
@RestController
@RequestMapping("/api")
public class LocalResource {

    private final Logger log = LoggerFactory.getLogger(LocalResource.class);

    private static final String ENTITY_NAME = "local";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalService localService;

    private final LocalRepository localRepository;

    public LocalResource(LocalService localService, LocalRepository localRepository) {
        this.localService = localService;
        this.localRepository = localRepository;
    }

    /**
     * {@code POST  /locals} : Create a new local.
     *
     * @param local the local to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new local, or with status {@code 400 (Bad Request)} if the local has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locals")
    public ResponseEntity<Local> createLocal(@Valid @RequestBody Local local) throws URISyntaxException {
        log.debug("REST request to save Local : {}", local);
        if (local.getId() != null) {
            throw new BadRequestAlertException("A new local cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Local result = localService.save(local);
        return ResponseEntity
            .created(new URI("/api/locals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /locals/:id} : Updates an existing local.
     *
     * @param id the id of the local to save.
     * @param local the local to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated local,
     * or with status {@code 400 (Bad Request)} if the local is not valid,
     * or with status {@code 500 (Internal Server Error)} if the local couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locals/{id}")
    public ResponseEntity<Local> updateLocal(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Local local)
        throws URISyntaxException {
        log.debug("REST request to update Local : {}, {}", id, local);
        if (local.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, local.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Local result = localService.save(local);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, local.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /locals/:id} : Partial updates given fields of an existing local, field will ignore if it is null
     *
     * @param id the id of the local to save.
     * @param local the local to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated local,
     * or with status {@code 400 (Bad Request)} if the local is not valid,
     * or with status {@code 404 (Not Found)} if the local is not found,
     * or with status {@code 500 (Internal Server Error)} if the local couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/locals/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Local> partialUpdateLocal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Local local
    ) throws URISyntaxException {
        log.debug("REST request to partial update Local partially : {}, {}", id, local);
        if (local.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, local.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Local> result = localService.partialUpdate(local);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, local.getId().toString())
        );
    }

    /**
     * {@code GET  /locals} : get all the locals.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locals in body.
     */
    @GetMapping("/locals")
    public ResponseEntity<List<Local>> getAllLocals(Pageable pageable) {
        log.debug("REST request to get a page of Locals");
        Page<Local> page = localService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /locals/:id} : get the "id" local.
     *
     * @param id the id of the local to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the local, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locals/{id}")
    public ResponseEntity<Local> getLocal(@PathVariable Long id) {
        log.debug("REST request to get Local : {}", id);
        Optional<Local> local = localService.findOne(id);
        return ResponseUtil.wrapOrNotFound(local);
    }

    /**
     * {@code DELETE  /locals/:id} : delete the "id" local.
     *
     * @param id the id of the local to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locals/{id}")
    public ResponseEntity<Void> deleteLocal(@PathVariable Long id) {
        log.debug("REST request to delete Local : {}", id);
        localService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
