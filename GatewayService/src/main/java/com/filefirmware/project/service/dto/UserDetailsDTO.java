package com.filefirmware.project.service.dto;


import com.filefirmware.project.config.Constants;

import com.filefirmware.project.domain.Authority;
import com.filefirmware.project.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDetailsDTO extends UserDTO{

    public UserDetailsDTO(){
        super();
    }

    public UserDetailsDTO(User user, String birthdate){
        super(user);
        this.birthdate=birthdate;
    }

    @Size(max = 50)
    private String birthdate;

    @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + this.getLogin() + '\'' +
            ", firstName='" + this.getFirstName()+ '\'' +
            ", lastName='" + this.getLastName() + '\'' +
            ", email='" + this.getEmail() + '\'' +
            ", imageUrl='" + this.getImageUrl() + '\'' +
            ", activated=" + this.isActivated() +
            ", langKey='" + this.getLangKey() + '\'' +
            ", createdBy=" + this.getCreatedBy() +
            ", createdDate=" + this.getCreatedDate() +
            ", lastModifiedBy='" + this.getLastModifiedBy() + '\'' +
            ", lastModifiedDate=" + this.getLastModifiedDate() +
            ", authorities=" + this.getAuthorities() +'\''+
            ", birthdate="+birthdate+
            "}";
    }
}
