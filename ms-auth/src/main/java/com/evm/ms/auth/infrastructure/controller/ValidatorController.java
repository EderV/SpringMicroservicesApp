package com.evm.ms.auth.infrastructure.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/val")
public class ValidatorController {

    /**
     * This endpoint is for ms-gateway to verify the clients before redirect it to another microservice. If everything
     * is correct, the endpoint will return 200 else, the security filter chain will return 401 or 403
     * @return http code 200
     */
    @GetMapping("/**")
    public ResponseEntity<String> validateRoutes() {
        return ResponseEntity.ok("OK");
    }

}
