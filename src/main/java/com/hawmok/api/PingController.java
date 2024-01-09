package com.hawmok.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping() {
        return "Hawmok-Server!";
    }

    @GetMapping("/v2/ping")
    public Boolean pingV2() {
        return true;
    }
}

