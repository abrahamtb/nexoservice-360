package com.nexoservice.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedWebController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}
