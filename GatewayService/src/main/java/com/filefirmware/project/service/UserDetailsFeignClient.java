package com.filefirmware.project.service;

import com.filefirmware.project.service.dto.UserDetailsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import javax.xml.ws.Response;

@FeignClient(name="userdetails")
public interface UserDetailsFeignClient {

    @PostMapping("/api/saveUserDetails")
    public ResponseEntity<UserDetailsDTO> registerUserDetails(UserDetailsDTO userDetailsDTO);
}
