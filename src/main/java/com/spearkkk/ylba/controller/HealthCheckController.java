package com.spearkkk.ylba.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @Value("${app.name}")
    public String name;

    @RequestMapping(value = {"/hello", "/api/hello"})
    public String hello() {
        return "Hello, I'm " + name + ".";
    }
}
