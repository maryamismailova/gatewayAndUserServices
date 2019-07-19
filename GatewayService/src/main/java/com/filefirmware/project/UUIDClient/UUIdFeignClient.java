package com.filefirmware.project.UUIDClient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="userservice")

public interface UUIdFeignClient {

    @GetMapping("/api/exists/{uuid}")
    ResponseEntity<Boolean> findUser(@PathVariable(value="uuid")  String uuid);

}
