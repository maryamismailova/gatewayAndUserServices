package com.filefirmware.project.service.impl;

import com.filefirmware.project.service.UuidHolderService;
import com.filefirmware.project.domain.UuidHolder;
import com.filefirmware.project.repository.UuidHolderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link UuidHolder}.
 */
@Service
@Transactional
public class UuidHolderServiceImpl implements UuidHolderService {

    private final Logger log = LoggerFactory.getLogger(UuidHolderServiceImpl.class);

    private final UuidHolderRepository uuidHolderRepository;

    public UuidHolderServiceImpl(UuidHolderRepository uuidHolderRepository) {
        this.uuidHolderRepository = uuidHolderRepository;
    }

    /**
     * Save a uuidHolder.
     *
     * @param uuidHolder the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UuidHolder save(UuidHolder uuidHolder) {
        log.debug("Request to save UuidHolder : {}", uuidHolder);
        return uuidHolderRepository.save(uuidHolder);
    }

    /**
     * Get all the uuidHolders.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UuidHolder> findAll() {
        log.debug("Request to get all UuidHolders");
        return uuidHolderRepository.findAll();
    }


    /**
     * Get one uuidHolder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UuidHolder> findOne(Long id) {
        log.debug("Request to get UuidHolder : {}", id);
        return uuidHolderRepository.findById(id);
    }

    /**
     * Delete the uuidHolder by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UuidHolder : {}", id);
        uuidHolderRepository.deleteById(id);
    }

    @Override
    public boolean exists(String uuid) {
        return uuidHolderRepository.existsByUuid(uuid);
    }
}
