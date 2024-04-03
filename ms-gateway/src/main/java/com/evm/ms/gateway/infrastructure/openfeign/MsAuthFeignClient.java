package com.evm.ms.gateway.infrastructure.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "ms-auth-client",
        url = "http://localhost:8081",
        configuration = FeignConfig.class
)
public interface MsAuthFeignClient {

    @GetMapping(value = "/api/auth/val{path}")
    String validateClient(@RequestHeader("Authorization") String authHeader, @PathVariable String path);

}
