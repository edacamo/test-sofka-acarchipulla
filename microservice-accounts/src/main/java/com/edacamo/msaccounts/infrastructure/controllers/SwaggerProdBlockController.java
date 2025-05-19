package com.edacamo.msaccounts.infrastructure.controllers;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Profile("prod")
public class SwaggerProdBlockController {

    @GetMapping({"/swagger-ui", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/swagger"})
    public String blockSwaggerAccess() {
        return "forward:/swagger-blocked.html";
    }
}
