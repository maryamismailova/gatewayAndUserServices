package com.filefirmware.project.repository;

import com.filefirmware.project.domain.UserDetailsDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserDetailsDTO entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDetailsDTORepository extends JpaRepository<UserDetailsDTO, Long> {

}
