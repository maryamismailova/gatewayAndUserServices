package com.filefirmware.project.service;

import com.filefirmware.project.domain.UserDetailsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserDetailsDTO}.
 */
public interface UserDetailsDTOService {

    /**
     * Save a userDetailsDTO.
     *
     * @param userDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    UserDetailsDTO save(UserDetailsDTO userDetailsDTO);

    /**
     * Get all the userDetailsDTOS.
     *
     * @return the list of entities.
     */
    List<UserDetailsDTO> findAll();


    /**
     * Get the "id" userDetailsDTO.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserDetailsDTO> findOne(Long id);

    /**
     * Delete the "id" userDetailsDTO.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
