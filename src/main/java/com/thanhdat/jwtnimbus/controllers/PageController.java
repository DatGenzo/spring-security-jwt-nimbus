package com.thanhdat.jwtnimbus.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {
    @GetMapping({"/", "/login"}) String login() { return "login"; }
    @GetMapping("/profile") String profile() { return "profile"; }
}
