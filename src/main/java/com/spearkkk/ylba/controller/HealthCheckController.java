package com.spearkkk.ylba.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @Value("${app.name}")
    public String name;

    @GetMapping(value = {"/hello", "/"})
    public String hello() {
        return "Hello, I'm " + name + ".";
    }
}
