package com.filefirmware.project.web.rest;

import com.filefirmware.project.domain.UuidHolder;
import com.filefirmware.project.service.UuidHolderService;
import com.filefirmware.project.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.filefirmware.project.domain.UuidHolder}.
 */
@RestController
@RequestMapping("/api")
public class UuidHolderResource {

    private final Logger log = LoggerFactory.getLogger(UuidHolderResource.class);

    private static final String ENTITY_NAME = "userserviceUuidHolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UuidHolderService uuidHolderService;

    public UuidHolderResource(UuidHolderService uuidHolderService) {
        this.uuidHolderService = uuidHolderService;
    }

    /**
     * {@code POST  /uuid-holders} : Create a new uuidHolder.
     *
     * @param uuidHolder the uuidHolder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uuidHolder, or with status {@code 400 (Bad Request)} if the uuidHolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uuid-holders")
    public ResponseEntity<UuidHolder> createUuidHolder(@RequestBody UuidHolder uuidHolder) throws URISyntaxException {
        log.debug("REST request to save UuidHolder : {}", uuidHolder);
        if (uuidHolder.getId() != null) {
            throw new BadRequestAlertException("A new uuidHolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UuidHolder result = uuidHolderService.save(uuidHolder);
        return ResponseEntity.created(new URI("/api/uuid-holders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uuid-holders} : Updates an existing uuidHolder.
     *
     * @param uuidHolder the uuidHolder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uuidHolder,
     * or with status {@code 400 (Bad Request)} if the uuidHolder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uuidHolder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uuid-holders")
    public ResponseEntity<UuidHolder> updateUuidHolder(@RequestBody UuidHolder uuidHolder) throws URISyntaxException {
        log.debug("REST request to update UuidHolder : {}", uuidHolder);
        if (uuidHolder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UuidHolder result = uuidHolderService.save(uuidHolder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, uuidHolder.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /uuid-holders} : get all the uuidHolders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uuidHolders in body.
     */
    @GetMapping("/uuid-holders")
    public List<UuidHolder> getAllUuidHolders() {
        log.debug("REST request to get all UuidHolders");
        return uuidHolderService.findAll();
    }

    /**
     * {@code GET  /uuid-holders/:id} : get the "id" uuidHolder.
     *
     * @param id the id of the uuidHolder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uuidHolder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uuid-holders/{id}")
    public ResponseEntity<UuidHolder> getUuidHolder(@PathVariable Long id) {
        log.debug("REST request to get UuidHolder : {}", id);
        Optional<UuidHolder> uuidHolder = uuidHolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uuidHolder);
    }

    /**
     * {@code DELETE  /uuid-holders/:id} : delete the "id" uuidHolder.
     *
     * @param id the id of the uuidHolder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uuid-holders/{id}")
    public ResponseEntity<Void> deleteUuidHolder(@PathVariable Long id) {
        log.debug("REST request to delete UuidHolder : {}", id);
        uuidHolderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/exists/{uuid}")
    public ResponseEntity<Boolean> existsByUuid(@PathVariable String uuid){
        log.debug("REST request to find if uuid exists");
        boolean exists=uuidHolderService.exists(uuid);
        if (exists)return new ResponseEntity<>(true, HttpStatus.FOUND);
        else return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}
