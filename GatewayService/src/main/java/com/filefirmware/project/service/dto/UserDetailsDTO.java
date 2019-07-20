package com.filefirmware.project.service.dto;



import com.filefirmware.project.config.Constants;

import com.filefirmware.project.domain.Authority;
import com.filefirmware.project.domain.User;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDetailsDTO extends UserDTO{

    public UserDetailsDTO(){
        super();
    }

    public UserDetailsDTO(User user, LocalDate birthdate){
        super(user);
        this.birthdate=birthdate;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthdate;

    public void setBirthdate(LocalDate birthdate){this.birthdate=birthdate;}
    public LocalDate getBirthdate(){return this.birthdate;}

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
            ", birthdate="+this.getBirthdate().toString()+
            "}";
    }
}
