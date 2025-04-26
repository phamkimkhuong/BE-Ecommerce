package com.backend.repositories.openFeignClient;

import com.backend.config.FeignClientConfig;
import com.backend.dtos.userDTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", configuration = FeignClientConfig.class)
public interface UserClient {
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    Object createUser(@RequestBody UserDTO user);
}
