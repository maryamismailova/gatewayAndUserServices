package com.filefirmware.project.service;

import com.filefirmware.project.domain.UuidHolder;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UuidHolder}.
 */
public interface UuidHolderService {

    /**
     * Save a uuidHolder.
     *
     * @param uuidHolder the entity to save.
     * @return the persisted entity.
     */
    UuidHolder save(UuidHolder uuidHolder);

    /**
     * Get all the uuidHolders.
     *
     * @return the list of entities.
     */
    List<UuidHolder> findAll();


    /**
     * Get the "id" uuidHolder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UuidHolder> findOne(Long id);

    /**
     * Delete the "id" uuidHolder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    boolean exists(String uuid);

}
