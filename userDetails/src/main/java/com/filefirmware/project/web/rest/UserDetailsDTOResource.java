package com.filefirmware.project.web.rest;

import com.filefirmware.project.domain.UserDetailsDTO;
import com.filefirmware.project.service.UserDetailsDTOService;
import com.filefirmware.project.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for managing {@link com.filefirmware.project.domain.UserDetailsDTO}.
 */
@RestController
@RequestMapping("/api")
public class UserDetailsDTOResource {

    private final Logger log = LoggerFactory.getLogger(UserDetailsDTOResource.class);

    private static final String ENTITY_NAME = "userDetailsUserDetailsDto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDetailsDTOService userDetailsDTOService;

    public UserDetailsDTOResource(UserDetailsDTOService userDetailsDTOService) {
        this.userDetailsDTOService = userDetailsDTOService;
    }

    /**
     * {@code POST  /user-details-dtos} : Create a new userDetailsDTO.
     *
     * @param userDetailsDTO the userDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDetailsDTO, or with status {@code 400 (Bad Request)} if the userDetailsDTO has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/saveUserDetails")
    public boolean createUserDetailsDTO(@RequestBody UserDetailsDTO userDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save UserDetailsDTO : {}", userDetailsDTO);
        if (userDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new userDetailsDTO cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDetailsDTO result = userDetailsDTOService.save(userDetailsDTO);
//        return ResponseEntity.created(new URI("/api/user-details-dtos/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
//            .body(result);
        return (result==null)? false:true;
    }

    /**
     * {@code PUT  /user-details-dtos} : Updates an existing userDetailsDTO.
     *
     * @param userDetailsDTO the userDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the userDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-details-dtos")
    public ResponseEntity<UserDetailsDTO> updateUserDetailsDTO(@RequestBody UserDetailsDTO userDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update UserDetailsDTO : {}", userDetailsDTO);
        if (userDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserDetailsDTO result = userDetailsDTOService.save(userDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-details-dtos} : get all the userDetailsDTOS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDetailsDTOS in body.
     */
    @GetMapping("/user-details-dtos")
    public List<UserDetailsDTO> getAllUserDetailsDTOS() {
        log.debug("REST request to get all UserDetailsDTOS");
        return userDetailsDTOService.findAll();
    }

    /**
     * {@code GET  /user-details-dtos/:id} : get the "id" userDetailsDTO.
     *
     * @param id the id of the userDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-details-dtos/{id}")
    public ResponseEntity<UserDetailsDTO> getUserDetailsDTO(@PathVariable Long id) {
        log.debug("REST request to get UserDetailsDTO : {}", id);
        Optional<UserDetailsDTO> userDetailsDTO = userDetailsDTOService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userDetailsDTO);
    }

    /**
     * {@code DELETE  /user-details-dtos/:id} : delete the "id" userDetailsDTO.
     *
     * @param id the id of the userDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-details-dtos/{id}")
    public ResponseEntity<Void> deleteUserDetailsDTO(@PathVariable Long id) {
        log.debug("REST request to delete UserDetailsDTO : {}", id);
        userDetailsDTOService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/getAsyncString")
    public String getAsyncString() throws Exception {

        CompletableFuture<String> name=userDetailsDTOService.getName();
        CompletableFuture<String> surname=userDetailsDTOService.getSurname();
        CompletableFuture.allOf(name, surname).join();
        String expression=name.get()+" "+surname.get();
        return expression;
    }

}
