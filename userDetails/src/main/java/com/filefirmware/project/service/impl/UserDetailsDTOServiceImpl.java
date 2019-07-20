package com.filefirmware.project.service.impl;

import com.filefirmware.project.service.UserDetailsDTOService;
import com.filefirmware.project.domain.UserDetailsDTO;
import com.filefirmware.project.repository.UserDetailsDTORepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service Implementation for managing {@link UserDetailsDTO}.
 */
@Service
@Transactional
public class UserDetailsDTOServiceImpl implements UserDetailsDTOService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsDTOServiceImpl.class);

    private final UserDetailsDTORepository userDetailsDTORepository;

    public UserDetailsDTOServiceImpl(UserDetailsDTORepository userDetailsDTORepository) {
        this.userDetailsDTORepository = userDetailsDTORepository;
    }

    /**
     * Save a userDetailsDTO.
     *
     * @param userDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public UserDetailsDTO save(UserDetailsDTO userDetailsDTO) {
        log.debug("Request to save UserDetailsDTO : {}", userDetailsDTO);
        return userDetailsDTORepository.save(userDetailsDTO);
    }

    /**
     * Get all the userDetailsDTOS.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDetailsDTO> findAll() {
        log.debug("Request to get all UserDetailsDTOS");
        return userDetailsDTORepository.findAll();
    }


    /**
     * Get one userDetailsDTO by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<UserDetailsDTO> findOne(Long id) {
        log.debug("Request to get UserDetailsDTO : {}", id);
        return userDetailsDTORepository.findById(id);
    }

    /**
     * Delete the userDetailsDTO by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserDetailsDTO : {}", id);
        userDetailsDTORepository.deleteById(id);
    }

    @Override
    public CompletableFuture<String> getName()throws Exception {

        Thread.sleep(1000);
        return CompletableFuture.completedFuture("Hello");
    }

    @Override
    public CompletableFuture<String> getSurname() throws Exception{
        Thread.sleep(900);
        return CompletableFuture.completedFuture("World!");
    }
}
