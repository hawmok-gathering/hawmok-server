package com.hawmock.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/success")
    public String successHandler() {
        return "success";
    }

    @GetMapping("/fail")
    public String failHandler() {
        return "fail";
    }
}
