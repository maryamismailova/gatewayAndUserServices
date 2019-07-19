package com.filefirmware.project.web.rest;

import com.filefirmware.project.UUIDClient.UUIdFeignClient;
import com.filefirmware.project.security.jwt.JWTFilter;
import com.filefirmware.project.security.jwt.TokenProvider;
import com.filefirmware.project.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.UUID;

/**
 * Controller to authenticate users.
 */

@RestController
@RequestMapping("/api")
public class UserJWTController {

    @Autowired
    UUIdFeignClient uuIdFeignClient;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }
    public static UUID makeUuid(String uuidString) {
        String[] parts = {
            uuidString.substring(0, 7),
            uuidString.substring(9, 12),
            uuidString.substring(14, 17),
            uuidString.substring(19, 22),
            uuidString.substring(24, 35)
        };
        long m1 = Long.parseLong(parts[0], 16);
        long m2 = Long.parseLong(parts[1], 16);
        long m3 = Long.parseLong(parts[2], 16);
        long lsb1 = Long.parseLong(parts[3], 16);
        long lsb2 = Long.parseLong(parts[4], 16);
        long msb = (m1 << 32) | (m2 << 16) | m3;
        long lsb = (lsb1 << 48) | lsb2;
        return new UUID(msb, lsb);
    }
    @PostMapping("/authenticate/{uuid}")
    public ResponseEntity<JWTToken> authorize(@Valid @PathVariable("uuid") String uuid) {

//        UUID uuid=makeUuid(uuidString);
        if(uuIdFeignClient.findUser(uuid).getBody()) {

            UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(uuid, uuid);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean rememberMe = true;

            String jwt = tokenProvider.createToken(authentication, rememberMe);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
