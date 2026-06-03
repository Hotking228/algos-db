package com.hotking.algosdb.controller;

import com.hotking.algosdb.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class Test {

    private final EmailService emailService;

    @GetMapping("/")
    public String setMessage(){
        emailService.sendMessage("kraulov@bk.ru", "code", "12345");
        return "/algo/algos";
    }
}
