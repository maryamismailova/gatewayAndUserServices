package com.filefirmware.project.repository;

import com.filefirmware.project.domain.UuidHolder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UuidHolder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UuidHolderRepository extends JpaRepository<UuidHolder, Long> {

    boolean existsByUuid(String uuid);
}
